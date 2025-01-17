package org.rhw.bmr.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import groovy.util.logging.Slf4j;
import org.rhw.bmr.user.common.biz.user.UserContext;
import org.rhw.bmr.user.dao.entity.GroupDO;
import org.rhw.bmr.user.dto.req.BmrGroupSortReqDTO;
import org.rhw.bmr.user.dto.req.BmrGroupUpdateDTO;
import org.rhw.bmr.user.remote.ShortLinkRemoteService;
import org.rhw.bmr.user.remote.dto.resp.ShortLinkCountQueryRespDTO;
import org.rhw.bmr.user.toolkit.RandomGenerator;
import org.rhw.bmr.user.common.convention.result.Result;
import org.rhw.bmr.user.dao.mapper.GroupMapper;
import org.rhw.bmr.user.dto.resp.BmrGroupRespDTO;
import org.rhw.bmr.user.service.GroupService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * 短链接分组接口实现
 */
@Slf4j
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {

    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {
    };

    @Override
    public void saveGroup(String groupName) {
        String gid;
        String name = UserContext.getUsername();

        do{
            gid = RandomGenerator.generateRandomString();
        }while(!hasGid(name, gid));

        GroupDO groupDO = GroupDO.builder()
                .gid(RandomGenerator.generateRandomString())
                .sortOrder(0)
                .username(name)
                .name(groupName)
                .build();
        baseMapper.insert(groupDO);
    }

    @Override
    public void saveGroup(String userName, String groupName) {
        String gid;

        do{
            gid = RandomGenerator.generateRandomString();
        }while(!hasGid(userName, gid));

        GroupDO groupDO = GroupDO.builder()
                .gid(RandomGenerator.generateRandomString())
                .sortOrder(0)
                .username(userName)
                .name(groupName)
                .build();
        baseMapper.insert(groupDO);
    }

    @Override
    public List<BmrGroupRespDTO> listGroup() {
        LambdaQueryWrapper<GroupDO> groupDOLambdaQueryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .orderByDesc(GroupDO::getSortOrder, GroupDO::getUpdateTime);

        List<GroupDO> groupDOList = baseMapper.selectList(groupDOLambdaQueryWrapper);

        Result<List<ShortLinkCountQueryRespDTO>> listResult = shortLinkRemoteService
                .listGroupShortLinkCount(groupDOList.stream().map(GroupDO::getGid).toList());

        List<BmrGroupRespDTO> shortLinkGroupRespDTPList = BeanUtil.copyToList(groupDOList, BmrGroupRespDTO.class);
        shortLinkGroupRespDTPList.forEach(each -> {

            Optional<ShortLinkCountQueryRespDTO> first = listResult.getData().stream()
                    .filter(item -> Objects.equals(item.getGid(), each.getGid()))
                    .findFirst();

            each.setShortLinkCount(first.isPresent() ? first.get().getShortLinkCount() : 0);

        });
        return shortLinkGroupRespDTPList;
    }

    @Override
    public void updateGroup(BmrGroupUpdateDTO requestParam) {
        LambdaUpdateWrapper<GroupDO> updateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getGid, requestParam.getGid());

        GroupDO groupDO = new GroupDO();
        groupDO.setName(requestParam.getName());
        baseMapper.update(groupDO, updateWrapper);
    }

    @Override
    public void deleteGroup(String gid) {
        LambdaUpdateWrapper<GroupDO> updateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getGid, gid);

        GroupDO groupDO = new GroupDO();
        groupDO.setDelFlag(1);
        baseMapper.update(groupDO, updateWrapper);
    }

    @Override
    public void sortGroup(List<BmrGroupSortReqDTO> requestParam) {
        requestParam.forEach(each->{
            GroupDO groupDO = GroupDO.builder().sortOrder(each.getSortOrder()).build();

            LambdaUpdateWrapper<GroupDO> updateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                    .eq(GroupDO::getGid, each.getGid())
                    .eq(GroupDO::getDelFlag, 0)
                    .eq(GroupDO::getUsername, UserContext.getUsername());

            baseMapper.update(groupDO, updateWrapper);
        });
    }

    private boolean hasGid(String userName, String gid) {
        LambdaQueryWrapper<GroupDO> eq = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getGid, gid)
                //TODO:等待网关完成之后，把用户信息传过来
                .eq(GroupDO::getUsername, userName);
        GroupDO hasGroupFlag = baseMapper.selectOne(eq);
        return hasGroupFlag == null;
    }
}
