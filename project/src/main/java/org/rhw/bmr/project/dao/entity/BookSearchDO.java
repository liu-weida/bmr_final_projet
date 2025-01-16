package org.rhw.bmr.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.rhw.bmr.project.common.database.baseDO;

/**
 * 访问日志监控
 */
@Data
@TableName("t_link_access_logs")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookSearchDO extends baseDO {
    private Long ref_id;
    private String title;
    private String reference;
    private String author;
    private String category;
    private String description;
    private String language;
    private Long clickCount;
    private Long sortedOrder;
}
