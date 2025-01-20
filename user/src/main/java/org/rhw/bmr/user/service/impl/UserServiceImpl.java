package org.rhw.bmr.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.rhw.bmr.user.common.constant.RedisCacheConstant;
import org.rhw.bmr.user.dao.entity.UserDO;
import org.rhw.bmr.user.dao.mapper.UserMapper;
import org.rhw.bmr.user.dto.req.UserLoginReqDTO;
import org.rhw.bmr.user.dto.req.UserRegisterReqDTO;
import org.rhw.bmr.user.dto.req.UserUpdateReqDTO;
import org.rhw.bmr.user.dto.resp.UserLoginRespDTO;
import org.rhw.bmr.user.dto.resp.UserRespDTO;
import org.rhw.bmr.user.service.UserService;
import org.rhw.bmr.user.common.convention.exception.ClientException;
import org.rhw.bmr.user.common.enums.UserErrorCodeEnum;
import org.rhw.bmr.user.service.GroupService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.rhw.bmr.user.common.constant.RedisCacheConstant.USER_LOGIN_KEY;
import static org.rhw.bmr.user.common.enums.UserErrorCodeEnum.USER_LOGIN_REPEAT;
import static org.rhw.bmr.user.common.enums.UserErrorCodeEnum.USER_NOT_ONLINE;


/**
 * 用户接口实现
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    private final RedissonClient redissonClient;    //使用分布式锁
    private final RRateLimiter userRegisterRateLimiter; //限流桶
    private final StringRedisTemplate redisTemplate;    //redis缓存查询
    private final GroupService groupService;

    @Override
    public UserRespDTO getUserByUsername(String username) {
        /*
        这里相当于 SELECT * FROM user WHERE username = username;
         */
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                                                    .eq(UserDO::getUsername, username);
        // 这里的baseMapper实际上就是我们传进去的UserMapper
        // selectOne，只找一条相符的数据
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        // 这里还存在问题，如果找不到怎么办？直接返回NULL用户就无法分辨到底是报错了，还是没找到?
        // 所以我们创建一个规约convention
        if (userDO == null) {
            throw new ClientException(UserErrorCodeEnum.USER_NULL);
        }

        UserRespDTO result = new UserRespDTO();
        BeanUtils.copyProperties(userDO, result);
        return result;
    }

    @Override
    public Boolean availableUsername(String username) {
        return !userRegisterCachePenetrationBloomFilter.contains(username);
    }

    @Override
    public void register(UserRegisterReqDTO requestParam) {

        if (!userRegisterRateLimiter.tryAcquire(1)) {
            throw new ClientException(UserErrorCodeEnum.USER_REGISTER_RATE_ERROR);
        }

        if (!availableUsername(requestParam.getUsername())) {
            throw new ClientException(UserErrorCodeEnum.USER_NAME_EXIST);
        }

        //拿个锁，然后所有请求都会针对用户名去拿锁
        RLock lock = redissonClient.getLock(RedisCacheConstant.LOCK_USER_REGISTER_KEY+requestParam.getUsername());

        //如果拿得到锁，说明能创建
        try {
            if (lock.tryLock()) {
                try{
                    int inserted = baseMapper.insert(BeanUtil.toBean(requestParam, UserDO.class));
                    if (inserted < 1){
                        throw new ClientException(UserErrorCodeEnum.USER_SAVE_ERROR);
                    }
                    userRegisterCachePenetrationBloomFilter.add(requestParam.getUsername());

                    groupService.saveGroup(requestParam.getUsername(),"默认分组");
                    return;
                }catch (DuplicateKeyException e){
                    throw new ClientException(UserErrorCodeEnum.USER_NAME_EXIST);
                }
            }
            // 拿不到锁，要么有人在注册，要么已经存在，不做分辨
            throw new ClientException(UserErrorCodeEnum.USER_NAME_EXIST);
        } finally {
           lock.unlock();
        }
    }

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        /*
         * 首先需要验证用户名是否存在数据库，然后验证密码，同时不能是已经注销的用户
         */
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername())
                .eq(UserDO::getPassword, requestParam.getPassword())
                .eq(UserDO::getDelFlag, 0);
        UserDO user = baseMapper.selectOne(queryWrapper);

        if (user == null) {
            throw new ClientException(UserErrorCodeEnum.USER_NULL);
        }

        if (redisTemplate.hasKey(USER_LOGIN_KEY+requestParam.getUsername())){
            // 相比直接抛出异常，顶替登录是否好一些？
            throw new ClientException(USER_LOGIN_REPEAT);
            // 下面就是顶替登录的代码
            // redisTemplate.expire(USER_LOGIN_KEY+requestParam.getUsername(), 0, TimeUnit.SECONDS);
        }

        /**
         * 考虑用Hash结构，Key是用户名，Value是(token，Json用户信息)
         * 这样做到用户名唯一性。
         */

        String uuid = UUID.randomUUID().toString();
        redisTemplate.opsForHash().put(USER_LOGIN_KEY+requestParam.getUsername(), uuid, JSON.toJSONString(user));
        redisTemplate.expire(USER_LOGIN_KEY+requestParam.getUsername(), 30L, TimeUnit.MINUTES);

        return new UserLoginRespDTO(uuid);
    }

    @Override
    public Boolean checkLogin(String username, String token) {

        // 检查是否登录，只要检查Redis里是否存在这个Token就行了。
        return redisTemplate.opsForHash().get(USER_LOGIN_KEY + username, token) != null;

    }

    @Override
    public void update(UserUpdateReqDTO requestParam) {
        // TODO: 验证当前用户是否为登录用户
        LambdaUpdateWrapper<UserDO> updateWrapper = Wrappers.lambdaUpdate(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername());
        baseMapper.update(BeanUtil.toBean(requestParam, UserDO.class), updateWrapper);
    }

    @Override
    public void logout(String username, String token) {
        if (checkLogin(username, token)){
            redisTemplate.delete(USER_LOGIN_KEY+username);
        }else{
            throw new ClientException(USER_NOT_ONLINE);
        }
    }
}
