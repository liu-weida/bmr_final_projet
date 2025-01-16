package org.rhw.bmr.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rhw.bmr.project.dao.entity.ShortLinkDO;
import org.rhw.bmr.project.dao.mapper.ShortLinkMapper;
import org.rhw.bmr.project.dto.req.RecycleBinRecoverReqDTO;
import org.rhw.bmr.project.dto.req.RecycleBinRemoveReqDTO;
import org.rhw.bmr.project.dto.req.RecycleBinSaveReqDTO;
import org.rhw.bmr.project.dto.req.ShortLinkRecycleBinPageReqDTO;
import org.rhw.bmr.project.dto.resp.ShortLinkPageRespDTO;
import org.rhw.bmr.project.service.RecycleBinService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import static org.rhw.bmr.project.common.constant.RedisKeyConstant.GOTO_IS_NULL_SHORT_LINK_KEY;
import static org.rhw.bmr.project.common.constant.RedisKeyConstant.GOTO_SHORT_LINK_KEY;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecycleBinServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements RecycleBinService {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void saveRecycleBin(RecycleBinSaveReqDTO requestParam) {
        LambdaUpdateWrapper<ShortLinkDO> updateWrapper = Wrappers.lambdaUpdate(ShortLinkDO.class)
                .eq(ShortLinkDO::getGid, requestParam.getGid())
                .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(ShortLinkDO::getDelFlag, 0)
                .eq(ShortLinkDO::getEnableStatus, 0);

        ShortLinkDO shortLinkDO = ShortLinkDO.builder().enableStatus(1).build();

        baseMapper.update(shortLinkDO, updateWrapper);

        // 删除缓存里的内容
        stringRedisTemplate.delete(String.format(GOTO_SHORT_LINK_KEY, requestParam.getFullShortUrl()));

    }

    @Override
    public IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam) {
        LambdaQueryWrapper<ShortLinkDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .in(ShortLinkDO::getGid, requestParam.getGidList())
                .eq(ShortLinkDO::getDelFlag, 0)
                .eq(ShortLinkDO::getEnableStatus, 1)
                .orderByDesc(ShortLinkDO::getUpdateTime);
        IPage<ShortLinkDO> resultPage = baseMapper.selectPage(requestParam, queryWrapper);
        return resultPage.convert(each ->{
            ShortLinkPageRespDTO result = BeanUtil.toBean(each, ShortLinkPageRespDTO.class);
            result.setDomain("http://" + result.getDomain());
            return result;
        });

    }

    @Override
    public void recoverRecycleBin(RecycleBinRecoverReqDTO requestParam) {
        LambdaUpdateWrapper<ShortLinkDO> updateWrapper = Wrappers.lambdaUpdate(ShortLinkDO.class)
                .eq(ShortLinkDO::getGid, requestParam.getGid())
                .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(ShortLinkDO::getDelFlag, 0)
                .eq(ShortLinkDO::getEnableStatus, 1);

        ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                .enableStatus(0)
                .build();

        baseMapper.update(shortLinkDO, updateWrapper);

        // 删除缓存里的内容
        stringRedisTemplate.delete(String.format(GOTO_IS_NULL_SHORT_LINK_KEY, requestParam.getFullShortUrl()));
    }

    @Override
    public void removeRecycleBin(RecycleBinRemoveReqDTO requestParam) {
        LambdaUpdateWrapper<ShortLinkDO> updateWrapper = Wrappers.lambdaUpdate(ShortLinkDO.class)
                .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(ShortLinkDO::getGid, requestParam.getGid())
                .eq(ShortLinkDO::getEnableStatus, 1)
                .eq(ShortLinkDO::getDelTime, 0L)
                .eq(ShortLinkDO::getDelFlag, 0);
        ShortLinkDO delShortLinkDO = ShortLinkDO.builder()
                .delTime(System.currentTimeMillis())
                .build();
        delShortLinkDO.setDelFlag(1);
        baseMapper.update(delShortLinkDO, updateWrapper);
    }

}
