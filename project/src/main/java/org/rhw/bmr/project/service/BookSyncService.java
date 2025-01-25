package org.rhw.bmr.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.rhw.bmr.project.dao.entity.BookSyncDO;

import java.util.List;

public interface BookSyncService extends IService<BookSyncDO> {
    List<BookSyncDO> getUnsyncedBooks(int limit);

    void updateSyncFlag(List<Long> ids);

    void syncBooksToElasticsearch();
}
