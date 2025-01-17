package org.rhw.bmr.project.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.SourceConfig;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.rhw.bmr.project.dao.entity.BookSearchDO;
import org.rhw.bmr.project.dao.mapper.BookSearchMapper;
import org.rhw.bmr.project.dto.req.BookSearchByWordReqDTO;
import org.rhw.bmr.project.dto.req.BookSearchReqDTO;
import org.rhw.bmr.project.dto.resp.BookSearchByWordRespDTO;
import org.rhw.bmr.project.dto.resp.BookSearchRespDTO;
import org.rhw.bmr.project.service.BookSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookSearchServiceImpl extends ServiceImpl<BookSearchMapper, BookSearchDO> implements BookSearchService {

    @Autowired
    private ElasticsearchClient elasticsearchClient;
    @Override
    public IPage<BookSearchRespDTO> pageBookSearchPage(BookSearchReqDTO requestParam) {

        // 检查分页参数（假设 Request 中包含分页参数，如 pageNo 和 pageSize）
        if (requestParam.getPageNo() == null || requestParam.getPageNo() < 1) {
            requestParam.setPageNo(1); // 默认页码为 1
        }
        if (requestParam.getPageSize() == null || requestParam.getPageSize() < 1) {
            requestParam.setPageSize(10); // 默认每页大小为 10
        }

        // 创建分页对象
        Page<BookSearchDO> page = new Page<>(requestParam.getPageNo(), requestParam.getPageSize());

        // 构造查询条件
        LambdaQueryWrapper<BookSearchDO> queryWrapper = Wrappers.lambdaQuery(BookSearchDO.class);
        if (requestParam.getTitle() != null && !requestParam.getTitle().isEmpty()) {
            queryWrapper.eq(BookSearchDO::getTitle, requestParam.getTitle());
        }
        if (requestParam.getAuthor() != null && !requestParam.getAuthor().isEmpty()) {
            queryWrapper.eq(BookSearchDO::getAuthor, requestParam.getAuthor());
        }
        if (requestParam.getCategory() != null && !requestParam.getCategory().isEmpty()) {
            queryWrapper.eq(BookSearchDO::getCategory, requestParam.getCategory());
        }
        if (requestParam.getLanguage() != null && !requestParam.getLanguage().isEmpty()) {
            queryWrapper.eq(BookSearchDO::getLanguage, requestParam.getLanguage());
        }

        // 设置排序
        queryWrapper.orderByDesc(BookSearchDO::getClickCount);

        // 执行分页查询
        IPage<BookSearchDO> bookSearchDOPage = baseMapper.selectPage(page, queryWrapper);

        // 转换分页结果为响应 DTO
        IPage<BookSearchRespDTO> bookSearchRespDTOPage = bookSearchDOPage.convert(bookSearchDO -> BookSearchRespDTO.builder()
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

        // 返回分页结果
        return bookSearchRespDTOPage;
    }

    @Override
    public IPage<BookSearchByWordRespDTO> pageBookSearchByWord(BookSearchByWordReqDTO requestParam) {


        if (requestParam.getWord() == null || requestParam.getWord().isEmpty()){

            log.error("搜索词为空");
            return null;

        }
        try {
            // 构建分页参数
            int pageNo = requestParam.getPageNo() != null ? requestParam.getPageNo() : 1;
            int pageSize = requestParam.getPageSize() != null ? requestParam.getPageSize() : 10;
            int from = (pageNo - 1) * pageSize;

            // 构建查询请求
            SearchRequest searchRequest = SearchRequest.of(sr -> sr
                    .index("bmr_books") // 替换为你的索引名称
                    .query(q -> q
                            .matchPhrase(m -> m
                                    .field("content")
                                    .query(requestParam.getWord()) // 按关键词查询
                            )
                    )
                    .source(s -> s
                            .filter(f -> f
                                    .includes("id","title", "author", "refId", "category", "language") // 指定返回字段
                            )
                    )
                    .from(from) // 设置分页开始位置
                    .size(pageSize) // 设置每页大小
            );


            // 执行查询
            SearchResponse<BookSearchByWordRespDTO> response = elasticsearchClient.search(
                    searchRequest,
                    BookSearchByWordRespDTO.class
            );

            // 获取查询结果
            List<BookSearchByWordRespDTO> records = new ArrayList<>();
            for (Hit<BookSearchByWordRespDTO> hit : response.hits().hits()) {
                records.add(hit.source());
            }

            // 获取总记录数
            long total = response.hits().total() != null
                    ? response.hits().total().value()
                    : 0;

            // 构建分页结果
            IPage<BookSearchByWordRespDTO> page = new Page<>(pageNo, pageSize, total);
            page.setRecords(records);
            return page;

        } catch (Exception e) {
            log.error("Elasticsearch 查询失败", e);
            return new Page<>(); // 返回空分页
        }
    }


}
