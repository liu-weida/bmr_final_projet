package org.rhw.bmr.user.controller;

import lombok.RequiredArgsConstructor;
import org.rhw.bmr.user.common.convention.result.Result;
import org.rhw.bmr.user.remote.ShortLinkRemoteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UrlTitleController {
    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {};

    @GetMapping("/api/bmr/user/v1/title")
    public Result<String> getTitleByUrl(@RequestParam("url") String url){
        return shortLinkRemoteService.getTitleByUrl(url);
    }

}
