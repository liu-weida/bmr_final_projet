package org.rhw.bmr.project.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.rhw.bmr.project.dao.entity.BookDO;
import org.rhw.bmr.project.dao.mapper.BookMapper;
import org.rhw.bmr.project.dto.req.BookSearchByRegexpReqDTO;
import org.rhw.bmr.project.dto.req.BookSearchByWordReqDTO;
import org.rhw.bmr.project.dto.req.BookSearchReqDTO;
import org.rhw.bmr.project.dto.resp.BookSearchByRegespRespDTO;
import org.rhw.bmr.project.dto.resp.BookSearchByWordRespDTO;
import org.rhw.bmr.project.dto.resp.BookSearchRespDTO;
import org.rhw.bmr.project.service.BookSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BookSearchServiceImpl extends ServiceImpl<BookMapper, BookDO> implements BookSearchService {

    @Autowired
    private ElasticsearchClient elasticsearchClient;
    @Override
    public IPage<BookSearchRespDTO> pageBookSearchPage(BookSearchReqDTO requestParam) {

        // 无论用户传什么分页参数，都只取前10本
        Page<BookDO> page = new Page<>(1, 10);

        // 构造查询条件
        LambdaQueryWrapper<BookDO> queryWrapper = Wrappers.lambdaQuery(BookDO.class);
        if (requestParam.getTitle() != null && !requestParam.getTitle().isEmpty()) {
            queryWrapper.eq(BookDO::getTitle, requestParam.getTitle());
        }
        if (requestParam.getAuthor() != null && !requestParam.getAuthor().isEmpty()) {
            queryWrapper.eq(BookDO::getAuthor, requestParam.getAuthor());
        }
        if (requestParam.getCategory() != null && !requestParam.getCategory().isEmpty()) {
            queryWrapper.eq(BookDO::getCategory, requestParam.getCategory());
        }
        if (requestParam.getLanguage() != null && !requestParam.getLanguage().isEmpty()) {
            queryWrapper.eq(BookDO::getLanguage, requestParam.getLanguage());
        }

        // 按 clickCount 降序排序
        queryWrapper.orderByDesc(BookDO::getClickCount);

        // 执行分页查询
        IPage<BookDO> bookSearchDOPage = baseMapper.selectPage(page, queryWrapper);

        // 转换为响应 DTO
        IPage<BookSearchRespDTO> bookSearchRespDTOPage = bookSearchDOPage.convert(bookSearchDO ->
                BookSearchRespDTO.builder()
                        .id(bookSearchDO.getId())
                        .refId(bookSearchDO.getRefId())
                        .title(bookSearchDO.getTitle())
                        .storagePath(bookSearchDO.getStoragePath())
                        .author(bookSearchDO.getAuthor())
                        .category(bookSearchDO.getCategory())
                        .description(bookSearchDO.getDescription())
                        .language(bookSearchDO.getLanguage())
                        .clickCount(bookSearchDO.getClickCount())
                        .sortedOrder(bookSearchDO.getSortedOrder())
                        .build()
        );

        return bookSearchRespDTOPage;
    }


    @Override
    public IPage<BookSearchByWordRespDTO> pageBookSearchByWord(BookSearchByWordReqDTO requestParam) {

        try {
            // 固定只查前10条
            int pageNo = 1;
            int pageSize = 10;
            int from = 0;

            // 构建查询请求
            SearchRequest searchRequest = SearchRequest.of(sr -> sr
                    .index("bmr_books")
                    .query(q -> q
                            .matchPhrase(m -> m
                                    .field("content")
                                    .query(requestParam.getWord())
                            )
                    )
                    .sort(sort -> sort
                            .field(f -> f
                                    .field("clickCount")            // 按 clickCount 排序
                                    .order(SortOrder.Desc)          // 降序
                            )
                    )
                    .source(s -> s
                            .filter(f -> f
                                    .includes("id","title", "author", "refId", "category", "language", "clickCount")
                            )
                    )
                    .from(from)
                    .size(pageSize)
            );

            // 执行查询
            SearchResponse<BookSearchByWordRespDTO> response = elasticsearchClient.search(
                    searchRequest, BookSearchByWordRespDTO.class
            );

            List<BookSearchByWordRespDTO> records = new ArrayList<>();
            for (Hit<BookSearchByWordRespDTO> hit : response.hits().hits()) {
                records.add(hit.source());
            }

            // 获取总记录数
            long total = response.hits().total() != null ? response.hits().total().value() : 0;

            // 组装分页对象
            IPage<BookSearchByWordRespDTO> page = new Page<>(pageNo, pageSize, total);
            page.setRecords(records);
            return page;

        } catch (Exception e) {
            log.error("Elasticsearch 查询失败", e);
            return new Page<>(); // 返回空分页
        }
    }


    @Override
    public IPage<BookSearchByRegespRespDTO> pageBookSearchByRegexp(BookSearchByRegexpReqDTO requestParam) {

        try {
            // 固定只查前10条
            int pageNo = 1;
            int pageSize = 10;
            int from = 0;

            // 构建查询请求
            SearchRequest searchRequest = SearchRequest.of(sr -> sr
                    .index("bmr_books")
                    .query(q -> q
                            .regexp(r -> r
                                    .field("content")
                                    .value(requestParam.getRegularExpr())
                            )
                    )
                    .sort(sort -> sort
                            .field(f -> f
                                    .field("clickCount")            // 按 clickCount 排序
                                    .order(SortOrder.Desc)          // 降序
                            )
                    )
                    .source(s -> s
                            .filter(f -> f
                                    .includes("id","title", "author", "refId", "category", "language", "clickCount")
                            )
                    )
                    .from(from)
                    .size(pageSize)
            );

            // 执行查询
            SearchResponse<BookSearchByRegespRespDTO> response = elasticsearchClient.search(
                    searchRequest, BookSearchByRegespRespDTO.class
            );

            // 获取查询结果
            List<BookSearchByRegespRespDTO> records = new ArrayList<>();
            for (Hit<BookSearchByRegespRespDTO> hit : response.hits().hits()) {
                records.add(hit.source());
            }

            // 获取总记录数
            long total = response.hits().total() != null ? response.hits().total().value() : 0;

            // 构建分页结果
            IPage<BookSearchByRegespRespDTO> page = new Page<>(pageNo, pageSize, total);
            page.setRecords(records);
            return page;

        } catch (Exception e) {
            log.error("Elasticsearch 查询失败", e);
            return new Page<>(); // 返回空分页
        }
    }



}
