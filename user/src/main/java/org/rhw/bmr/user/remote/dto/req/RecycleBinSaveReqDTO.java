package org.rhw.bmr.user.remote.dto.req;

import lombok.Data;

/**
 * 回收站请求参数
 */
@Data
public class RecycleBinSaveReqDTO {
    private String gid;
    private String fullShortUrl;
}
