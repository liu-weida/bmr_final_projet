package org.rhw.bmr.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.rhw.bmr.user.common.biz.user.UserContext;
import org.rhw.bmr.user.dao.entity.GroupDO;
import org.rhw.bmr.user.remote.ShortLinkRemoteService;
import org.rhw.bmr.user.remote.dto.resp.ShortLinkPageRespDTO;
import org.rhw.bmr.user.service.RecycleBinService;
import org.rhw.bmr.user.common.convention.result.Result;
import org.rhw.bmr.user.dao.mapper.GroupMapper;
import org.rhw.bmr.user.dto.req.ShortLinkRecycleBinPageReqDTO;
import org.rhw.bmr.project.common.convention.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecycleBinServiceImpl implements RecycleBinService {

    private final GroupMapper groupMapper;
    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {};

    @Override
    public Result<IPage<ShortLinkPageRespDTO>> pageRecycleBinShortLink(ShortLinkRecycleBinPageReqDTO requestParam) {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getDelFlag, 0);

        List<GroupDO> groupDO = groupMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(groupDO)){
            throw new ServiceException("用户无分组信息");
        }

        requestParam.setGidList(groupDO.stream().map(GroupDO::getGid).toList());

        return shortLinkRemoteService.pageRecycleBinShortLink(requestParam);
    }
}


