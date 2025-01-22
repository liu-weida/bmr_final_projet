package org.rhw.bmr.project.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.rhw.bmr.project.dao.entity.BookDO;
import org.rhw.bmr.project.dao.mapper.BookMapper;
import org.rhw.bmr.project.dto.req.ReadBookReqDTO;
import org.rhw.bmr.project.dto.resp.ReadBookRespDTO;
import org.rhw.bmr.project.service.ReadBookService;
import org.springframework.stereotype.Service;

@Service
public class ReadBookImpl extends ServiceImpl<BookMapper, BookDO> implements ReadBookService {

    @Override
    public ReadBookRespDTO readBook(ReadBookReqDTO requestParam) {

        BookDO bookDO = baseMapper.selectById(requestParam.getBookId());

        bookDO.getStoragePath();

        if (bookDO != null) {
            bookDO.setClickCount(bookDO.getClickCount() + 1);
            baseMapper.updateById(bookDO);
            return new ReadBookRespDTO(bookDO.getStoragePath(),bookDO.getImg());
        }

        // 如果未找到对应的书籍，返回一个默认响应或抛出异常
        return new ReadBookRespDTO(); // 或者抛出一个自定义异常
    }

}
