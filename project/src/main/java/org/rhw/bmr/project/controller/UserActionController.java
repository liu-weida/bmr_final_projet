package org.rhw.bmr.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.rhw.bmr.project.common.convention.result.Result;
import org.rhw.bmr.project.common.convention.result.Results;
import org.rhw.bmr.project.dto.req.BookSearchReqDTO;
import org.rhw.bmr.project.dto.req.BookmarkReqDTO;
import org.rhw.bmr.project.dto.req.BookmarkSearchReqDTO;
import org.rhw.bmr.project.dto.req.ReadBookReqDTO;
import org.rhw.bmr.project.dto.resp.BookSearchRespDTO;
import org.rhw.bmr.project.dto.resp.BookmarkSearchRespDTO;
import org.rhw.bmr.project.dto.resp.ReadBookRespDTO;
import org.rhw.bmr.project.service.BookmarkService;
import org.rhw.bmr.project.service.ReadBookService;
import org.rhw.bmr.project.service.UserPreferenceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserActionController {

    private final ReadBookService readBookService;
    private final UserPreferenceService userPreferenceService;
    private final BookmarkService bookmarkService;

    @GetMapping("/api/bmr/project/v1/readBook")
    public Result<ReadBookRespDTO> readBook(ReadBookReqDTO requestParam){

        userPreferenceService.recordUserPreference(requestParam);

        return Results.success(readBookService.readBook(requestParam));
    }

    @PostMapping("/api/bmr/project/v1/bookmark")
    public Result<Void> bookmark(BookmarkReqDTO requestParam){
        userPreferenceService.recordUserPreference(requestParam);
        bookmarkService.bookmark(requestParam);
        return Results.success();
    }

    @PostMapping("/api/bmr/project/v1/bookmark/delete")
    public Result<Void> bookmarkDelete(BookmarkReqDTO requestParam){
        bookmarkService.deleteBookmark(requestParam);
        return Results.success();
    }

    @GetMapping("/api/bmr/project/v1/bookmark/search")
    public Result<IPage<BookmarkSearchRespDTO>> bookmarkSearch(BookmarkSearchReqDTO requestParam){
        return Results.success(bookmarkService.bookmarkSearch(requestParam));
    }



}
