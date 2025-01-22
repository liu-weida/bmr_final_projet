package org.rhw.bmr.project.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.rhw.bmr.project.dao.entity.BookDO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookSearchByWordReqDTO {

    private String word;

    private Integer pageNo;   // 当前页码
    private Integer pageSize; // 每页大小

}
