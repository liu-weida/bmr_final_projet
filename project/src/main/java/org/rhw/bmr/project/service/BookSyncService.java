package org.rhw.bmr.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.rhw.bmr.project.dao.entity.BookSyncDO;

import java.util.List;

public interface BookSyncService extends IService<BookSyncDO> {
    // 查询未同步的数据
    List<BookSyncDO> getUnsyncedBooks(int limit);

    // 更新同步标记
    void updateSyncFlag(List<Long> ids);

    // 同步数据到 Elasticsearch
    void syncBooksToElasticsearch();
}
