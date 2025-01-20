package org.rhw.bmr.project.controller;

import lombok.RequiredArgsConstructor;
import org.rhw.bmr.project.common.convention.result.Result;
import org.rhw.bmr.project.common.convention.result.Results;
import org.rhw.bmr.project.dao.entity.BookDO;
import org.rhw.bmr.project.dao.entity.UserPreferenceDO;
import org.rhw.bmr.project.dto.req.ReadBookReqDTO;
import org.rhw.bmr.project.dto.resp.ReadBookRespDTO;
import org.rhw.bmr.project.service.RecommendationService;
import org.rhw.bmr.project.service.UserPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/api/bmr/project/v1/recommend")
    public Result<List<BookDO>> RecommendBook(@RequestParam("user_id") Long userId){
        return Results.success(recommendationService.recommendBooksForUser(userId));
    }

}
