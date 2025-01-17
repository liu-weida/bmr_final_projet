package org.rhw.bmr.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.rhw.bmr.project.dao.entity.BookSearchDO;
import org.rhw.bmr.project.dto.req.BookSearchByRegexpReqDTO;
import org.rhw.bmr.project.dto.req.BookSearchByWordReqDTO;
import org.rhw.bmr.project.dto.req.BookSearchReqDTO;
import org.rhw.bmr.project.dto.resp.BookSearchByRegespRespDTO;
import org.rhw.bmr.project.dto.resp.BookSearchByWordRespDTO;
import org.rhw.bmr.project.dto.resp.BookSearchRespDTO;

import java.util.List;

public interface BookSearchService extends IService<BookSearchDO> {


    IPage<BookSearchRespDTO> pageBookSearchPage(BookSearchReqDTO requestParam);

    IPage<BookSearchByWordRespDTO> pageBookSearchByWord(BookSearchByWordReqDTO requestParam);

    IPage<BookSearchByRegespRespDTO> pageBookSearchByRegexp(BookSearchByRegexpReqDTO requestParam);
}
