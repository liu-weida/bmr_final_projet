package org.rhw.bmr.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.rhw.bmr.project.dao.entity.BookSearchDO;
import org.rhw.bmr.project.dao.entity.LinkStatsTodayDO;
import org.rhw.bmr.project.dto.req.BookSearchReqDTO;
import org.rhw.bmr.project.dto.resp.BookSearchRespDTO;

import java.util.List;

public interface BookSearchService extends IService<BookSearchDO> {


    List<BookSearchRespDTO> pageBookSearch(BookSearchReqDTO requestParam);

}
