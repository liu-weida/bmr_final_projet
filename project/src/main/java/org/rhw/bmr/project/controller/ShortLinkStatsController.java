package org.rhw.bmr.project.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.rhw.bmr.project.common.convention.result.Result;
import org.rhw.bmr.project.common.convention.result.Results;
import org.rhw.bmr.project.dto.req.ShortLinkGroupStatsAccessRecordReqDTO;
import org.rhw.bmr.project.dto.req.ShortLinkGroupStatsReqDTO;
import org.rhw.bmr.project.dto.req.ShortLinkStatsAccessRecordReqDTO;
import org.rhw.bmr.project.dto.req.ShortLinkStatsReqDTO;
import org.rhw.bmr.project.dto.resp.ShortLinkStatsAccessRecordRespDTO;
import org.rhw.bmr.project.dto.resp.ShortLinkStatsRespDTO;
import org.rhw.bmr.project.service.ShortLinkStatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ShortLinkStatsController {
    private final ShortLinkStatsService shortLinkStatsService;


    /**
     * 访问单个链接指定时间内的监控数据
     */
    @GetMapping("api/short-link/project/v1/stats")
    public Result<ShortLinkStatsRespDTO> shortLinkStats(ShortLinkStatsReqDTO requestParam) {
        return Results.success(shortLinkStatsService.oneShortLinkStats(requestParam));
    }

    /**
     * 访问分组短链接指定时间内监控数据
     */
    @GetMapping("/api/short-link/project/v1/stats/group")
    public Result<ShortLinkStatsRespDTO> groupShortLinkStats(ShortLinkGroupStatsReqDTO requestParam) {
        return Results.success(shortLinkStatsService.groupShortLinkStats(requestParam));
    }

    /**
     * 访问单个短链接指定时间内访问记录监控数据
     */
    @GetMapping("/api/short-link/project/v1/stats/access-record")
    public Result<IPage<ShortLinkStatsAccessRecordRespDTO>> shortLinkStatsAccessRecord(ShortLinkStatsAccessRecordReqDTO requestParam) {
        return Results.success(shortLinkStatsService.shortLinkStatsAccessRecord(requestParam));
    }

    /**
     * 访问分组短链接指定时间内访问记录监控数据
     */
    @GetMapping("/api/short-link/project/v1/stats/access-record/group")
    public Result<IPage<ShortLinkStatsAccessRecordRespDTO>> groupShortLinkStatsAccessRecord(ShortLinkGroupStatsAccessRecordReqDTO requestParam) {
        return Results.success(shortLinkStatsService.groupShortLinkStatsAccessRecord(requestParam));
    }

}
