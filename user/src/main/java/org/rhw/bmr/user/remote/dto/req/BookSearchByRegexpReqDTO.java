package org.rhw.bmr.user.remote.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookSearchByRegexpReqDTO {

    private String regularExpr;

    private Integer pageNo;   // 当前页码
    private Integer pageSize; // 每页大小
}
