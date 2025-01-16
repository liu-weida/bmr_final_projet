package org.rhw.bmr.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.rhw.bmr.project.dao.entity.BookSearchDO;
import org.rhw.bmr.project.dao.mapper.BookSearchMapper;
import org.rhw.bmr.project.dto.req.BookSearchReqDTO;
import org.rhw.bmr.project.dto.resp.BookSearchRespDTO;
import org.rhw.bmr.project.service.BookSearchService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookSearchServiceImpl extends ServiceImpl<BookSearchMapper, BookSearchDO> implements BookSearchService {


    @Override
    public List<BookSearchRespDTO> pageBookSearch(BookSearchReqDTO requestParam) {
        // 构造查询条件
        LambdaQueryWrapper<BookSearchDO> queryWrapper = Wrappers.lambdaQuery(BookSearchDO.class)
                .func(wrapper -> {
                    if (requestParam.getTitle() != null && !requestParam.getTitle().isEmpty()) {
                        wrapper.eq(BookSearchDO::getTitle, requestParam.getTitle());
                    }
                    if (requestParam.getAuthor() != null && !requestParam.getAuthor().isEmpty()) {
                        wrapper.eq(BookSearchDO::getAuthor, requestParam.getAuthor());
                    }
                    if (requestParam.getCategory() != null && !requestParam.getCategory().isEmpty()) {
                        wrapper.eq(BookSearchDO::getCategory, requestParam.getCategory());
                    }
                    if (requestParam.getLanguage() != null && !requestParam.getLanguage().isEmpty()) {
                        wrapper.eq(BookSearchDO::getLanguage, requestParam.getLanguage());
                    }
                });

        // 执行查询
        List<BookSearchDO> resultList = baseMapper.selectList(queryWrapper);

        // 将查询结果转换为响应 DTO
        return resultList.stream()
                .map(bookSearchDO -> new BookSearchRespDTO()) // 需实现转换逻辑
                .collect(Collectors.toList());
    }



}
