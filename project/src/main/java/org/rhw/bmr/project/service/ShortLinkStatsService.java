package org.rhw.bmr.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.rhw.bmr.project.dto.req.ShortLinkGroupStatsAccessRecordReqDTO;
import org.rhw.bmr.project.dto.req.ShortLinkGroupStatsReqDTO;
import org.rhw.bmr.project.dto.req.ShortLinkStatsAccessRecordReqDTO;
import org.rhw.bmr.project.dto.req.ShortLinkStatsReqDTO;
import org.rhw.bmr.project.dto.resp.ShortLinkStatsAccessRecordRespDTO;
import org.rhw.bmr.project.dto.resp.ShortLinkStatsRespDTO;

public interface ShortLinkStatsService {

    /**
     * 获取单个短链接监控数据
     *
     * @param requestParam 获取短链接监控数据入参
     * @return 短链接监控数据
     */
    ShortLinkStatsRespDTO oneShortLinkStats(ShortLinkStatsReqDTO requestParam);


    /**
     * 访问单个短链接指定时间内访问监控数据
     */
    IPage<ShortLinkStatsAccessRecordRespDTO> shortLinkStatsAccessRecord(ShortLinkStatsAccessRecordReqDTO requestParam);

    ShortLinkStatsRespDTO groupShortLinkStats(ShortLinkGroupStatsReqDTO requestParam);

    IPage<ShortLinkStatsAccessRecordRespDTO> groupShortLinkStatsAccessRecord(ShortLinkGroupStatsAccessRecordReqDTO requestParam);
}
