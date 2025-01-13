package org.rhw.bmr.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.rhw.bmr.project.dao.entity.ShortLinkDO;
import org.rhw.bmr.project.dto.req.RecycleBinRecoverReqDTO;
import org.rhw.bmr.project.dto.req.RecycleBinRemoveReqDTO;
import org.rhw.bmr.project.dto.req.RecycleBinSaveReqDTO;
import org.rhw.bmr.project.dto.req.ShortLinkRecycleBinPageReqDTO;
import org.rhw.bmr.project.dto.resp.ShortLinkPageRespDTO;

public interface RecycleBinService extends IService<ShortLinkDO> {

    /**
     * 保存回收站
     * @param requestParam 参数
     */
    void saveRecycleBin(RecycleBinSaveReqDTO requestParam);

    /**
     * 分页查询短链接
     *
     * @param requestParam 分页查询短链接请求参数
     * @return 短链接分页返回结果
     */
    IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam);

    /**
     * 回收站恢复短链接功能
     */
    void recoverRecycleBin(RecycleBinRecoverReqDTO requestParam);

    /**
     * 回收站删除短链功能
     */
    void removeRecycleBin(RecycleBinRemoveReqDTO requestParam);
}
