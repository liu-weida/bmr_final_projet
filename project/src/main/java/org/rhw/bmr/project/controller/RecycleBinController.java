package org.rhw.bmr.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.rhw.bmr.project.common.convention.result.Result;
import org.rhw.bmr.project.common.convention.result.Results;
import org.rhw.bmr.project.dto.req.RecycleBinRecoverReqDTO;
import org.rhw.bmr.project.dto.req.RecycleBinRemoveReqDTO;
import org.rhw.bmr.project.dto.req.RecycleBinSaveReqDTO;
import org.rhw.bmr.project.dto.req.ShortLinkRecycleBinPageReqDTO;
import org.rhw.bmr.project.dto.resp.ShortLinkPageRespDTO;
import org.rhw.bmr.project.service.RecycleBinService;
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

    @PostMapping("/api/short-link/project/v1/recycle-bin/save")
    public Result<Void> saveRecycleBin(@RequestBody RecycleBinSaveReqDTO requestParam){

        recycleBinService.saveRecycleBin(requestParam);

        return Results.success(null);
    }

    @GetMapping("/api/short-link/project/v1/recycle-bin/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam){
        return Results.success(recycleBinService.pageShortLink(requestParam));
    }

    /**
     * 回收站恢复功能
     */
    @PostMapping("/api/short-link/project/v1/recycle-bin/recover")
    public Result<Void> recoverRecycleBin(@RequestBody RecycleBinRecoverReqDTO requestParam){
        recycleBinService.recoverRecycleBin(requestParam);
        return Results.success(null);
    }

    /**
     * 回收站删除功能
     */
    @PostMapping("/api/short-link/project/v1/recycle-bin/remove")
    public Result<Void> removeRecycleBin(@RequestBody RecycleBinRemoveReqDTO requestParam){
        recycleBinService.removeRecycleBin(requestParam);
        return Results.success(null);
    }
}
