package org.rhw.bmr.project.controller;

import lombok.RequiredArgsConstructor;
import org.rhw.bmr.project.common.convention.result.Result;
import org.rhw.bmr.project.common.convention.result.Results;
import org.rhw.bmr.project.dto.req.ReadBookReqDTO;
import org.rhw.bmr.project.dto.resp.ReadBookRespDTO;
import org.rhw.bmr.project.service.ReadBookService;
import org.rhw.bmr.project.service.UserPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReadBookController {

    private final ReadBookService readBookService;
    private final UserPreferenceService userPreferenceService;

    @GetMapping("/api/bmr/project/v1/readBook")
    public Result<ReadBookRespDTO> readBook(ReadBookReqDTO requestParam){

        userPreferenceService.recordUserPreference(requestParam);

        return Results.success(readBookService.readBook(requestParam));
    }

}
