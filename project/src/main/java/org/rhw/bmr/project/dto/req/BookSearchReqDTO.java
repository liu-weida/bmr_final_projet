package org.rhw.bmr.project.dto.req;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookSearchReqDTO {

    private Long id;

    private String title;

    private String author;

    private String category;

    private String language;

    private Integer pageNo;   // 当前页码
    private Integer pageSize; // 每页大小
}
