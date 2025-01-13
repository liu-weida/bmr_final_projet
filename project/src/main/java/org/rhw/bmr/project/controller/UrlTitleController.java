package org.rhw.bmr.project.controller;


import lombok.RequiredArgsConstructor;
import org.rhw.bmr.project.common.convention.result.Result;
import org.rhw.bmr.project.common.convention.result.Results;
import org.rhw.bmr.project.service.UrlTitleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UrlTitleController {

    private final UrlTitleService urlTitleService;

    /**
     * 获取对应网站的标题
     * @param url   链接
     * @return  标题
     */
    @GetMapping("/api/short-link/project/v1/title")
    public Result<String> getTitleByUrl(@RequestParam("url") String url) {

        return Results.success(urlTitleService.getTitleByUrl(url));
    }
}
