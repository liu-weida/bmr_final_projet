package org.rhw.bmr.project.dto.req;

import lombok.Data;

/**
 * 回收站请求参数
 */
@Data
public class RecycleBinRemoveReqDTO {
    private String gid;
    private String fullShortUrl;
}
