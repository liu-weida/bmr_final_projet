package org.rhw.bmr.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.rhw.bmr.project.dao.entity.BookSearchDO;
import org.rhw.bmr.project.dto.req.BookSearchReqDTO;

import java.util.List;

public interface BookSearchMapper extends BaseMapper<BookSearchDO> {

    List<BookSearchDO> SearchBook(@Param("param")BookSearchReqDTO requestParam);

}
