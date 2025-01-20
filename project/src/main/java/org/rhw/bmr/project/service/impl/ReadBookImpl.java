package org.rhw.bmr.project.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.rhw.bmr.project.dao.entity.BookDO;
import org.rhw.bmr.project.dao.entity.BookSyncDO;
import org.rhw.bmr.project.dao.mapper.BookMapper;
import org.rhw.bmr.project.dao.mapper.BookSyncMapper;
import org.rhw.bmr.project.dto.req.ReadBookReqDTO;
import org.rhw.bmr.project.dto.resp.ReadBookRespDTO;
import org.rhw.bmr.project.service.BookSyncService;
import org.rhw.bmr.project.service.ReadBookService;
import org.rhw.bmr.project.service.UserPreferenceService;
import org.springframework.stereotype.Service;

@Service
public class ReadBookImpl extends ServiceImpl<BookMapper, BookDO> implements ReadBookService {

    @Override
    public ReadBookRespDTO readBook(ReadBookReqDTO requestParam) {

        LambdaQueryChainWrapper<BookDO> eq = lambdaQuery().eq(BookDO::getId, requestParam.getId());

        BookDO bookDO = eq.one();

        if (bookDO != null) {
            bookDO.setClickCount(bookDO.getClickCount() + 1);
            updateById(bookDO);
            return new ReadBookRespDTO(bookDO.getStoragePath());
        }

        // 如果未找到对应的书籍，返回一个默认响应或抛出异常
        return new ReadBookRespDTO(null); // 或者抛出一个自定义异常
    }

}
