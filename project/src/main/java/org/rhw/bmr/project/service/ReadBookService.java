package org.rhw.bmr.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.rhw.bmr.project.dao.entity.BookDO;
import org.rhw.bmr.project.dto.req.ReadBookReqDTO;
import org.rhw.bmr.project.dto.resp.ReadBookRespDTO;

public interface ReadBookService  extends IService<BookDO> {

    ReadBookRespDTO readBook(ReadBookReqDTO requestParam);



}
