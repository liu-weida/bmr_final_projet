package org.rhw.bmr.project.dto.req;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;

public class BookInsertionReqDTO {

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
    private String img;

}
