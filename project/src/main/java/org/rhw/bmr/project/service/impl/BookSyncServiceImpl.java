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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
            return;
        }

        LambdaUpdateWrapper<BookSyncDO> updateWrapper = Wrappers.lambdaUpdate(BookSyncDO.class)
                .in(BookSyncDO::getId, ids)
                .set(BookSyncDO::getEsSyncFlag, 1);

        baseMapper.update(null, updateWrapper);
    }


    @Override
    public void syncBooksToElasticsearch() {
        try {
            List<BookSyncDO> unsyncedBooks = getUnsyncedBooks(100);

            while (!unsyncedBooks.isEmpty()) {
                BulkRequest.Builder bulkRequestBuilder = new BulkRequest.Builder();

                for (BookSyncDO bookSyncDO : unsyncedBooks) {
                    String fileContent = readFileContent(bookSyncDO.getStoragePath());

                    Map<String, Object> document = new HashMap<>();
                    document.put("id", bookSyncDO.getId());
                    document.put("refId", bookSyncDO.getRefId());
                    document.put("title", bookSyncDO.getTitle());
                    document.put("author", bookSyncDO.getAuthor());
                    document.put("category", bookSyncDO.getCategory());
                    document.put("language", bookSyncDO.getLanguage());
                    document.put("content", fileContent);
                    document.put("clickCount", bookSyncDO.getClickCount());

                    bulkRequestBuilder.operations(op -> op
                            .index(idx -> idx
                                    .index("bmr_books")
                                    .id(String.valueOf(bookSyncDO.getId()))
                                    .document(document)
                            ));
                }

                BulkResponse bulkResponse = elasticsearchClient.bulk(bulkRequestBuilder.build());
                if (bulkResponse.errors()) {
                    log.warn("Some data failed to synchronize: {}", bulkResponse.items().stream()
                            .filter(item -> item.error() != null)
                            .map(item -> "ID: " + item.id() + ", Reason: " + item.error().reason())
                            .collect(Collectors.joining(", ")));
                }

                List<Long> syncedIds = unsyncedBooks.stream().map(BookSyncDO::getId).collect(Collectors.toList());
                updateSyncFlag(syncedIds);

                log.info("Successfully synchronized {} entries.", syncedIds.size());

                unsyncedBooks = getUnsyncedBooks(100);
            }

            log.info("Data synchronization complete！");
        } catch (IOException e) {
            log.error("File read failed", e);
        } catch (Exception e) {
            log.error("Failed to synchronize data to Elasticsearch", e);
        }
    }

    private String readFileContent(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return null;
        }

        try {
            if (filePath.startsWith("http://") || filePath.startsWith("https://")) {
                return readFromUrl(filePath);
            } else {
                return new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            log.error("Unable to read content: {}", filePath, e);
            return null;
        }
    }

    private String readFromUrl(String urlPath) throws IOException {
        StringBuilder content = new StringBuilder();
        while (true) {
            URL url = new URL(urlPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setInstanceFollowRedirects(false);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                urlPath = connection.getHeaderField("Location");
                log.info("redirect to: {}", urlPath);
                continue;
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            } finally {
                connection.disconnect();
            }

            break;
        }

        return content.toString();
    }
}