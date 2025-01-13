package org.rhw.bmr.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.rhw.bmr.user.common.convention.result.Result;
import org.rhw.bmr.user.dto.req.ShortLinkRecycleBinPageReqDTO;
import org.rhw.bmr.user.remote.dto.resp.ShortLinkPageRespDTO;

/**
 * URL回收站接口层
 */
public interface RecycleBinService {
    Result<IPage<ShortLinkPageRespDTO>> pageRecycleBinShortLink(ShortLinkRecycleBinPageReqDTO requestParam);
}
