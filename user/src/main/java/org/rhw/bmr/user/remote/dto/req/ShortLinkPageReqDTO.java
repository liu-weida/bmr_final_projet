package org.rhw.bmr.user.remote.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.rhw.bmr.project.dao.entity.ShortLinkDO;

/**
 * 短链接分页请求
 */
@Data
public class ShortLinkPageReqDTO extends Page<ShortLinkDO> {
    private String gid;

    /**
     * 排序标签
     */
    private String orderTag;
}
