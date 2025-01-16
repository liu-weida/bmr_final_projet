package org.rhw.bmr.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.rhw.bmr.user.common.convention.result.Result;
import org.rhw.bmr.user.remote.ShortLinkRemoteService;
import org.rhw.bmr.user.remote.dto.req.ShortLinkStatsReqDTO;
import org.rhw.bmr.user.remote.dto.resp.ShortLinkStatsRespDTO;
import org.rhw.bmr.project.dto.req.ShortLinkStatsAccessRecordReqDTO;
import org.rhw.bmr.project.dto.resp.ShortLinkStatsAccessRecordRespDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ShortLinkStatsController {

    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {};

    /**
     * 访问单个短链接指定时间内监控数据
     */
    @GetMapping("/api/bmr/user/v1/stats")
    public Result<ShortLinkStatsRespDTO> shortLinkStats(ShortLinkStatsReqDTO requestParam) {
        return shortLinkRemoteService.oneShortLinkStats(requestParam);
    }

    @GetMapping("/api/bmr/user/v1/stats/access-record")
    public Result<IPage<ShortLinkStatsAccessRecordRespDTO>> shortLinkAccessRecord(ShortLinkStatsAccessRecordReqDTO requestParam) {
        return shortLinkRemoteService.shortLinkStatsAccessRecord(requestParam);
    }


}