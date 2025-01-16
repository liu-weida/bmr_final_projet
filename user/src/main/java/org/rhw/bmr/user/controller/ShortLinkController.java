package org.rhw.bmr.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.rhw.bmr.user.common.convention.result.Result;
import org.rhw.bmr.user.common.convention.result.Results;
import org.rhw.bmr.user.remote.ShortLinkRemoteService;
import org.rhw.bmr.user.remote.dto.req.ShortLinkCreateReqDTO;
import org.rhw.bmr.user.remote.dto.req.ShortLinkPageReqDTO;
import org.rhw.bmr.user.remote.dto.req.ShortLinkUpdateReqDTO;
import org.rhw.bmr.user.remote.dto.resp.ShortLinkCreateRespDTO;
import org.rhw.bmr.user.remote.dto.resp.ShortLinkPageRespDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * 短链接后管控制层
 */
@RestController
public class ShortLinkController {

    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {};

    @GetMapping("/api/bmr/user/v1/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam){

        return shortLinkRemoteService.pageShortLink(requestParam);
    }

    @PostMapping("/api/bmr/user/v1/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam){

        return shortLinkRemoteService.createShortLink(requestParam);
    }

    @PostMapping("/api/bmr/user/v1/update")
    public Result<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam){
        shortLinkRemoteService.updateShortLink(requestParam);
        return Results.success();
    }





}
