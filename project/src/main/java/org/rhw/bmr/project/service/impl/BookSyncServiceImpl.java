package org.rhw.bmr.project.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.rhw.bmr.project.dao.entity.BookSyncDO;
import org.rhw.bmr.project.dao.mapper.BookSyncMapper;
import org.rhw.bmr.project.service.BookSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookSyncServiceImpl extends ServiceImpl<BookSyncMapper, BookSyncDO> implements BookSyncService {

    @Autowired
    private ElasticsearchClient elasticsearchClient;
    @Override
    public List<BookSyncDO> getUnsyncedBooks(int limit) {

        LambdaQueryWrapper<BookSyncDO> last = Wrappers.lambdaQuery(BookSyncDO.class)
                .eq(BookSyncDO::getEsSyncFlag, 0)
                .last("LIMIT " + limit);

        return baseMapper.selectList(last);
    }

    @Override
    public void updateSyncFlag(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return; // 如果 id 列表为空，直接返回
        }

        // 构造更新条件
        LambdaUpdateWrapper<BookSyncDO> updateWrapper = Wrappers.lambdaUpdate(BookSyncDO.class)
                .in(BookSyncDO::getId, ids) // 更新所有符合 id 列表的记录
                .set(BookSyncDO::getEsSyncFlag, 1); // 设置同步标志位为 1

        // 执行批量更新
        baseMapper.update(null, updateWrapper);
    }


    @Override
    public void syncBooksToElasticsearch() {
        log.info("开始同步数据到 Elasticsearch...");
        try {
            // 每次同步 100 条数据
            List<BookSyncDO> unsyncedBooks = getUnsyncedBooks(100);

            while (!unsyncedBooks.isEmpty()) {
                // 构造 BulkRequest
                BulkRequest.Builder bulkRequestBuilder = new BulkRequest.Builder();

                for (BookSyncDO bookSyncDO : unsyncedBooks) {
                    // 读取 storagePath 文件内容
                    String fileContent = readFileContent(bookSyncDO.getStoragePath());

                    // 构造文档
                    Map<String, Object> document = new HashMap<>();
                    document.put("id", bookSyncDO.getId());
                    document.put("refId", bookSyncDO.getRefId());
                    document.put("title", bookSyncDO.getTitle());
                    document.put("author", bookSyncDO.getAuthor());
                    document.put("category", bookSyncDO.getCategory());
                    document.put("language", bookSyncDO.getLanguage());
                    document.put("content", fileContent); // 添加文件内容

                    bulkRequestBuilder.operations(op -> op
                            .index(idx -> idx
                                    .index("bmr_books") // Elasticsearch 索引名称
                                    .id(String.valueOf(bookSyncDO.getId())) // 使用 ID 作为文档 ID
                                    .document(document)
                            ));
                }

                // 批量写入 Elasticsearch
                BulkResponse bulkResponse = elasticsearchClient.bulk(bulkRequestBuilder.build());
                if (bulkResponse.errors()) {
                    log.warn("部分数据同步失败: {}", bulkResponse.items().stream()
                            .filter(item -> item.error() != null)
                            .map(item -> "ID: " + item.id() + ", Reason: " + item.error().reason())
                            .collect(Collectors.joining(", ")));
                }

                // 更新同步标记
                List<Long> syncedIds = unsyncedBooks.stream().map(BookSyncDO::getId).collect(Collectors.toList());
                updateSyncFlag(syncedIds);

                log.info("已成功同步 {} 条数据", syncedIds.size());

                // 查询下一批数据
                unsyncedBooks = getUnsyncedBooks(100);
            }

            log.info("数据同步完成！");
        } catch (IOException e) {
            log.error("文件读取失败", e);
        } catch (Exception e) {
            log.error("同步数据到 Elasticsearch 失败", e);
        }
    }

    private String readFileContent(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return null; // 如果路径为空，返回 null
        }
        try {
            // 读取文件内容
            return new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("无法读取文件: {}", filePath, e);
            return null; // 如果读取失败，返回 null
        }
    }


}
