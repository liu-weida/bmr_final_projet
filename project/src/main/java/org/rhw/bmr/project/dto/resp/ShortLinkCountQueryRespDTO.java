package org.rhw.bmr.project.dto.resp;

import lombok.Data;

/**
 * 短链接分组下有多少链接查询返回
 */
@Data
public class ShortLinkCountQueryRespDTO {
    private String gid;
    private Integer shortLinkCount;

}
