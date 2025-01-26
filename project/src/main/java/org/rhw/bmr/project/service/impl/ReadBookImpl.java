package org.rhw.bmr.project.service.impl;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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

        BookDO bookDO = baseMapper.selectById(Long.valueOf(requestParam.getBookId()));


        if (bookDO != null) {

            LambdaUpdateWrapper<BookDO> wrapper = Wrappers.lambdaUpdate(BookDO.class)
                    .eq(BookDO::getId, Long.valueOf(bookDO.getId()))
                    .set(BookDO::getClickCount, bookDO.getClickCount() + 1);

            baseMapper.update(null, wrapper);

            return new ReadBookRespDTO(bookDO.getStoragePath(),bookDO.getImg());
        }

        return new ReadBookRespDTO();
    }

}
