package org.rhw.bmr.project.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("t_books")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookSearchDO extends baseDO {

    private Long id;

    @TableField("ref_id")
    private Long refId;

    private String title;

    @TableField("storage_path")
    private String storagePath;

    private String author;

    private String category;

    private String description;

    private String language;

    private Long clickCount;

    private Long sortedOrder;

    @TableField(value = "es_sync_flag",fill = FieldFill.INSERT)
    private Integer esSyncFlag;
}
