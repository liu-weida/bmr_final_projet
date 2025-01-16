package org.rhw.bmr.project.controller;

import lombok.RequiredArgsConstructor;
import org.rhw.bmr.project.common.convention.result.Result;
import org.rhw.bmr.project.common.convention.result.Results;
import org.rhw.bmr.project.dto.req.BookSearchReqDTO;
import org.rhw.bmr.project.dto.resp.BookSearchRespDTO;
import org.rhw.bmr.project.service.BookSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookSearchController {

    private final BookSearchService bookSearchService;

    @GetMapping("/api/bmr/project/v1/bookSearch")
    public Result<List<BookSearchRespDTO>> pageShortLink(BookSearchReqDTO requestParam){
        return Results.success(bookSearchService.pageBookSearch(requestParam));
    }


}
