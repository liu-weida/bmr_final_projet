package org.rhw.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.rhw.user.common.biz.user.UserContext;
import org.rhw.user.common.convention.exception.ClientException;
import org.rhw.user.common.convention.exception.ServiceException;
import org.rhw.user.common.convention.result.Result;
import org.rhw.user.dao.entity.GroupDO;
import org.rhw.user.dao.mapper.GroupMapper;
import org.rhw.user.dao.mapper.GroupUniqueMapper;
import org.rhw.user.dto.req.ShortLinkGroupSortReqDTO;
import org.rhw.user.dto.req.ShortLinkGroupUpdateReqDTO;
import org.rhw.user.dto.resp.ShortLinkGroupRespDTO;
import org.rhw.user.remote.ShortLinkActualRemoteService;
import org.rhw.user.remote.dto.resp.ShortLinkGroupCountQueryRespDTO;
import org.rhw.user.service.GroupService;
import org.rhw.user.toolkit.LocalUserGroupIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.rhw.user.common.constant.RedisCacheConstant.LOCK_GROUP_CREATE_KEY;

/**
 * 短链接分组接口实现层
 * */
@Slf4j // 启用日志记录功能，方便记录日志
@Service // 声明为 Spring 服务组件
@RequiredArgsConstructor // Lombok 注解，自动生成构造函数并注入所有 `final` 修饰的字段
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {

    // 远程服务，用于查询短链接分组的相关统计信息
    private final ShortLinkActualRemoteService shortLinkActualRemoteService;

    // Redisson 客户端，用于实现分布式锁等高级功能
    private final RedissonClient redissonClient;

    //基于redis的id生成器
    private final LocalUserGroupIdGenerator localUserGroupIdGenerator;

    // 配置文件中定义的最大分组数量
    @Value("${bmr-user.group.max-num}")
    private Integer groupMaxNum;

    /**
     * 创建默认分组
     */
    @Override
    public void saveGroup(String groupName) {
        // 调用重载方法，将当前用户的用户名和分组名传入
        saveGroup(UserContext.getUsername(), groupName);
    }

    /**
     * 创建分组（带用户名）
     */
    @Override
    public void saveGroup(String username, String groupName) {
        // 获取分布式锁，确保同一用户的分组创建是串行的
        RLock lock = redissonClient.getLock(String.format(LOCK_GROUP_CREATE_KEY, username));
        lock.lock(); // 加锁
        try {
            // 查询当前用户已有的分组
            LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                    .eq(GroupDO::getUsername, username)
                    .eq(GroupDO::getDelFlag, 0);
            List<GroupDO> groupDOList = baseMapper.selectList(queryWrapper);

            // 如果分组数量已达到最大限制，抛出异常
            if (CollUtil.isNotEmpty(groupDOList) && groupDOList.size() == groupMaxNum) {
                throw new ClientException(String.format("已超出最大分组数：%d", groupMaxNum));
            }

            String gid = saveGroupUniqueReturnGid(); // 生成 GID
            GroupDO groupDO = GroupDO.builder()
                    .gid(gid)
                    .sortOrder(0)
                    .username(username)
                    .name(groupName)
                    .build();
            baseMapper.insert(groupDO);

        } finally {
            lock.unlock(); // 解锁
        }
    }

    /**
     * 获取分组列表
     */
    @Override
    public List<ShortLinkGroupRespDTO> listGroup() {
        // 查询当前用户的所有有效分组，按排序值和更新时间倒序排列
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .orderByDesc(GroupDO::getSortOrder, GroupDO::getUpdateTime);
        List<GroupDO> groupDOList = baseMapper.selectList(queryWrapper);

        // 调用远程服务查询每个分组的短链接数量
        Result<List<ShortLinkGroupCountQueryRespDTO>> listResult = shortLinkActualRemoteService
                .listGroupShortLinkCount(groupDOList.stream().map(GroupDO::getGid).toList());

        // 将数据库实体对象转换为响应 DTO 对象
        List<ShortLinkGroupRespDTO> shortLinkGroupRespDTOList = BeanUtil.copyToList(groupDOList, ShortLinkGroupRespDTO.class);

        // 将远程服务返回的短链接数量信息整合到响应 DTO 中
        shortLinkGroupRespDTOList.forEach(each -> {
            Optional<ShortLinkGroupCountQueryRespDTO> first = listResult.getData().stream()
                    .filter(item -> Objects.equals(item.getGid(), each.getGid()))
                    .findFirst();
            first.ifPresent(item -> each.setShortLinkCount(first.get().getShortLinkCount()));
        });

        return shortLinkGroupRespDTOList; // 返回组装后的分组列表
    }

    /**
     * 更新分组信息
     */
    @Override
    public void updateGroup(ShortLinkGroupUpdateReqDTO requestParam) {
        // 构造更新条件
        LambdaUpdateWrapper<GroupDO> updateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getGid, requestParam.getGid())
                .eq(GroupDO::getDelFlag, 0);

        // 更新分组名称
        GroupDO groupDO = new GroupDO();
        groupDO.setName(requestParam.getName());
        baseMapper.update(groupDO, updateWrapper);
    }

    /**
     * 删除分组
     */
    @Override
    public void deleteGroup(String gid) {
        // 构造删除条件（逻辑删除）
        LambdaUpdateWrapper<GroupDO> updateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getGid, gid)
                .eq(GroupDO::getDelFlag, 0);

        // 设置删除标志位
        GroupDO groupDO = new GroupDO();
        groupDO.setDelFlag(1);
        baseMapper.update(groupDO, updateWrapper);
    }

    /**
     * 分组排序
     */
    @Override
    public void sortGroup(List<ShortLinkGroupSortReqDTO> requestParam) {
        // 遍历每个分组的排序请求
        requestParam.forEach(each -> {
            // 构造排序更新条件
            GroupDO groupDO = GroupDO.builder()
                    .sortOrder(each.getSortOrder())
                    .build();
            LambdaUpdateWrapper<GroupDO> updateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                    .eq(GroupDO::getUsername, UserContext.getUsername())
                    .eq(GroupDO::getGid, each.getGid())
                    .eq(GroupDO::getDelFlag, 0);

            // 更新排序值
            baseMapper.update(groupDO, updateWrapper);
        });
    }

    /**
     * 生成分组唯一标识（GID）
     */
    private String saveGroupUniqueReturnGid() {
        // 生成随机 GID
        String gid = localUserGroupIdGenerator.nextUserGroupID();

        return gid; // 返回生成的 GID
    }
}
