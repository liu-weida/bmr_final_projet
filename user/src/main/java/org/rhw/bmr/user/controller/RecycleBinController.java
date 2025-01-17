package org.rhw.bmr.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.rhw.bmr.user.service.RecycleBinService;
import org.rhw.bmr.user.common.convention.result.Result;
import org.rhw.bmr.user.common.convention.result.Results;
import org.rhw.bmr.user.dto.req.BmrRecycleBinPageReqDTO;
import org.rhw.bmr.user.remote.ShortLinkRemoteService;
import org.rhw.bmr.user.remote.dto.req.RecycleBinRecoverReqDTO;
import org.rhw.bmr.user.remote.dto.req.RecycleBinRemoveReqDTO;
import org.rhw.bmr.user.remote.dto.req.RecycleBinSaveReqDTO;
import org.rhw.bmr.user.remote.dto.resp.ShortLinkPageRespDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 回收站控制层
 */
@RestController
@RequiredArgsConstructor
public class RecycleBinController {

    private final RecycleBinService recycleBinService;

    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {};


    /**
     * 保存回收站
     */
    @PostMapping("/api/bmr/user/v1/recycle-bin/save")
    public Result<Void> saveRecycleBin(@RequestBody RecycleBinSaveReqDTO requestParam){

        shortLinkRemoteService.saveRecycleBin(requestParam);
        return Results.success();
    }

    /**
     * 回收站分页查询
     */
    @GetMapping("/api/bmr/user/v1/recycle-bin/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(BmrRecycleBinPageReqDTO requestParam){
        return recycleBinService.pageRecycleBinShortLink(requestParam);
    }

    /**
     * 回收站恢复功能
     */
    @PostMapping("/api/bmr/user/v1/recycle-bin/recover")
    public Result<Void> recoverRecycleBin(@RequestBody RecycleBinRecoverReqDTO requestParam){
        shortLinkRemoteService.recoverRecycleBin(requestParam);
        return Results.success(null);
    }

    /**
     * 回收站删除功能
     */
    @PostMapping("/api/bmr/user/v1/recycle-bin/remove")
    public Result<Void> removeRecycleBin(@RequestBody RecycleBinRemoveReqDTO requestParam){
        shortLinkRemoteService.removeRecycleBin(requestParam);
        return Results.success(null);
    }
}
