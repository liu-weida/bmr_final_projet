package org.rhw.bmr.project.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.rhw.bmr.project.common.convention.result.Result;
import org.rhw.bmr.project.common.convention.result.Results;
import org.rhw.bmr.project.dto.req.ShortLinkCreateReqDTO;
import org.rhw.bmr.project.dto.req.ShortLinkPageReqDTO;
import org.rhw.bmr.project.dto.req.ShortLinkUpdateReqDTO;
import org.rhw.bmr.project.dto.resp.ShortLinkCountQueryRespDTO;
import org.rhw.bmr.project.dto.resp.ShortLinkCreateRespDTO;
import org.rhw.bmr.project.dto.resp.ShortLinkPageRespDTO;
import org.rhw.bmr.project.handler.CustomBlockHandler;
import org.rhw.bmr.project.service.ShortLinkService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ShortLinkController {

    private final ShortLinkService shortLinkService;

    @PostMapping("/api/short-link/project/v1/create")
    @SentinelResource(
            value = "create_short-link",
            blockHandler = "createShortLinkBlockHandlerMethod",
            blockHandlerClass = CustomBlockHandler.class
    )
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam){

        return Results.success(shortLinkService.createShortLink(requestParam));
    }


    @PostMapping("/api/short-link/project/v1/update")
    public Result<Void>updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam){

        shortLinkService.updateShortLink(requestParam);

        return Results.success();
    }

    /**
     * 分页查询短链接
     */
    @GetMapping("/api/short-link/project/v1/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam){
        return Results.success(shortLinkService.pageShortLink(requestParam));
    }

    /**
     * 查询分组内短链接数量
     */
    @GetMapping("/api/short-link/project/v1/count")
    public Result<List<ShortLinkCountQueryRespDTO>> listGroupShortLinkCount(@RequestParam("requestParam") List<String> requestParam){
        return Results.success(shortLinkService.listGroupShortLinkCount(requestParam));
    }

    /**
     * 短链接跳转
     */
    @GetMapping("/{short-uri}")
    public void restoreUrl(@PathVariable("short-uri") String shortUri, HttpServletRequest request, HttpServletResponse response){
        shortLinkService.restoreUrl(shortUri, request, response);
    }
}
