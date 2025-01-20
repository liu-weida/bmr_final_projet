package org.rhw.bmr.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;

import org.rhw.bmr.user.common.convention.result.Result;
import org.rhw.bmr.user.common.convention.result.Results;
import org.rhw.bmr.user.remote.BookSearchRemoteService;

import org.rhw.bmr.user.remote.dto.req.*;

import org.rhw.bmr.user.remote.dto.resp.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 短链接后管控制层
 */
@RestController
public class BookSearchController {

    BookSearchRemoteService bookSearchRemoteService = new BookSearchRemoteService() {};

    @GetMapping("/api/bmr/user/v1/bookSearch_page")
    public Result<IPage<BookSearchRespDTO>> pageBookSearch(BookSearchReqDTO requestParam){

        return bookSearchRemoteService.bookSearch(requestParam);

    }

    @GetMapping("/api/bmr/user/v1/bookSearch_by_word")
    public Result<IPage<BookSearchByWordRespDTO>> pageBookSearchByWord(BookSearchByWordReqDTO requestParam){

        return bookSearchRemoteService.bookSearchByWord(requestParam);
    }

    @GetMapping("/api/bmr/user/v1/bookSearch_by_regexp")
    public Result<IPage<BookSearchByRegespRespDTO>> pageBookSearchByRegexp(BookSearchByRegexpReqDTO requestParam){
        return bookSearchRemoteService.bookSearchByRegexp(requestParam);
    }




}
