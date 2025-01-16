package org.rhw.bmr.user.dto.resp;

import lombok.Data;

@Data
public class ShortLinkGroupRespDTO {
    private String gid;
    private String name;
    private int sortOrder;

    /**
     * 当前分组下有多少个短链接
     */
    private Integer shortLinkCount;
}
