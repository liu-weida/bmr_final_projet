package org.rhw.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.rhw.user.common.biz.user.UserContext;
import org.rhw.user.common.convention.exception.ClientException;
import org.rhw.user.common.convention.exception.ServiceException;
import org.rhw.user.common.enums.UserErrorCodeEnum;
import org.rhw.user.dao.entity.UserDO;
import org.rhw.user.dao.mapper.UserMapper;
import org.rhw.user.dto.req.UserLoginReqDTO;
import org.rhw.user.dto.req.UserRegisterReqDTO;
import org.rhw.user.dto.req.UserUpdateReqDTO;
import org.rhw.user.dto.resp.UserLoginRespDTO;
import org.rhw.user.dto.resp.UserRespDTO;
import org.rhw.user.service.GroupService;
import org.rhw.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.rhw.user.common.constant.RedisCacheConstant.LOCK_USER_REGISTER_KEY;
import static org.rhw.user.common.constant.RedisCacheConstant.USER_LOGIN_KEY;
import static org.rhw.user.common.enums.UserErrorCodeEnum.USER_EXIST;
import static org.rhw.user.common.enums.UserErrorCodeEnum.USER_NAME_EXIST;
import static org.rhw.user.common.enums.UserErrorCodeEnum.USER_SAVE_ERROR;

/**
 * 用户接口实现层
 * */
@Service // 声明为 Spring 的服务组件，表示这是业务逻辑实现层
@RequiredArgsConstructor // Lombok 注解，自动生成构造器，注入所有 `final` 修饰的字段
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    // 分布式布隆过滤器，用于防止缓存穿透，存储已注册的用户名
    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;

    // Redisson 客户端，用于实现分布式锁等高级功能
    private final RedissonClient redissonClient;

    // Redis 操作模板，用于直接与 Redis 交互
    private final StringRedisTemplate stringRedisTemplate;

    // 分组服务，用户注册时会创建一个默认分组
    private final GroupService groupService;

    /**
     * 根据用户名获取用户信息
     */
    @Override
    public UserRespDTO getUserByUsername(String username) {
        // 创建 MyBatis-Plus 的查询条件，通过用户名查询用户信息
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);

        // 查询数据库
        UserDO userDO = baseMapper.selectOne(queryWrapper);

        // 如果用户不存在，抛出业务异常
        if (userDO == null) {
            throw new ServiceException(UserErrorCodeEnum.USER_NULL);
        }

        // 将数据库实体对象转换为响应 DTO 对象
        UserRespDTO result = new UserRespDTO();
        BeanUtils.copyProperties(userDO, result); // 属性拷贝
        return result;
    }

    /**
     * 检查用户名是否存在，使用布隆过滤器防止缓存穿透
     */
    @Override
    public Boolean hasUsername(String username) {
        // 如果布隆过滤器中不包含该用户名，说明用户不存在
        return !userRegisterCachePenetrationBloomFilter.contains(username);
    }

    /**
     * 用户注册
     */
    @Transactional(rollbackFor = Exception.class) // 声明事务，发生异常时回滚
    @Override
    public void register(UserRegisterReqDTO requestParam) {
        // 检查用户名是否已存在
        if (!hasUsername(requestParam.getUsername())) {
            throw new ClientException(USER_NAME_EXIST);
        }

        // 使用分布式锁防止并发注册
        RLock lock = redissonClient.getLock(LOCK_USER_REGISTER_KEY + requestParam.getUsername());
        if (!lock.tryLock()) { // 如果获取锁失败，抛出异常
            throw new ClientException(USER_NAME_EXIST); //当前用户名正在被注册
        }

        try {
            // 将注册信息转换为数据库实体并插入数据库
            int inserted = baseMapper.insert(BeanUtil.toBean(requestParam, UserDO.class));
            if (inserted < 1) { // 如果插入失败，抛出异常
                throw new ClientException(USER_SAVE_ERROR);
            }

            // 创建用户的默认分组
            groupService.saveGroup(requestParam.getUsername(), "默认分组");

            // 将用户名加入布隆过滤器，防止缓存穿透
            userRegisterCachePenetrationBloomFilter.add(requestParam.getUsername());
        } catch (DuplicateKeyException ex) { // 捕获主键冲突异常，抛出用户已存在异常
            throw new ClientException(USER_EXIST);
        } finally {
            // 释放分布式锁
            lock.unlock();
        }
    }

    /**
     * 更新用户信息
     */
    @Override
    public void update(UserUpdateReqDTO requestParam) {
        // 校验当前用户是否为登录用户
        if (!Objects.equals(requestParam.getUsername(), UserContext.getUsername())) {
            throw new ClientException("当前登录用户修改请求异常");
        }

        // 构造更新条件，通过用户名更新用户信息
        LambdaUpdateWrapper<UserDO> updateWrapper = Wrappers.lambdaUpdate(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername());

        // 执行更新操作
        baseMapper.update(BeanUtil.toBean(requestParam, UserDO.class), updateWrapper);
    }

    /**
     * 用户登录
     */
    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        // 构造查询条件，通过用户名、密码等条件查询用户
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername())
                .eq(UserDO::getPassword, requestParam.getPassword())
                .eq(UserDO::getDelFlag, 0);

        // 查询用户信息
        UserDO userDO = baseMapper.selectOne(queryWrapper);

        // 如果用户不存在，抛出异常
        if (userDO == null) {
            throw new ClientException("用户不存在");
        }

        // 从 Redis 检查是否已有登录信息
        Map<Object, Object> hasLoginMap = stringRedisTemplate.opsForHash().entries(USER_LOGIN_KEY + requestParam.getUsername());
        if (CollUtil.isNotEmpty(hasLoginMap)) {
            // 如果用户已登录，更新登录状态过期时间
            stringRedisTemplate.expire(USER_LOGIN_KEY + requestParam.getUsername(), 30L, TimeUnit.MINUTES);

            // 返回已存在的 token
            String token = hasLoginMap.keySet().stream()
                    .findFirst()
                    .map(Object::toString)
                    .orElseThrow(() -> new ClientException("用户登录错误"));
            return new UserLoginRespDTO(token);
        }

        // 生成新的登录 token
        String uuid = UUID.randomUUID().toString();

        // 将登录信息存入 Redis（使用 Hash 数据结构）
        stringRedisTemplate.opsForHash().put(USER_LOGIN_KEY + requestParam.getUsername(), uuid, JSON.toJSONString(userDO));
        stringRedisTemplate.expire(USER_LOGIN_KEY + requestParam.getUsername(), 30L, TimeUnit.MINUTES);

        // 返回登录成功的 token
        return new UserLoginRespDTO(uuid);
    }

    /**
     * 检查用户是否已登录
     */
    @Override
    public Boolean checkLogin(String username, String token) {
        // 检查 Redis 中是否存在对应的 token
        return stringRedisTemplate.opsForHash().get(USER_LOGIN_KEY + username, token) != null;
    }

    /**
     * 用户登出
     */
    @Override
    public void logout(String username, String token) {
        // 验证用户是否已登录
        if (checkLogin(username, token)) {
            // 删除 Redis 中的登录信息
            stringRedisTemplate.delete(USER_LOGIN_KEY + username);
            return;
        }

        // 如果用户未登录或 token 不存在，抛出异常
        throw new ClientException("用户Token不存在或用户未登录");
    }
}
