package org.rhw.bmr.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.rhw.bmr.project.dao.entity.ShortLinkDO;
import org.rhw.bmr.project.dto.biz.ShortLinkStatsRecordDTO;
import org.rhw.bmr.project.dto.req.ShortLinkCreateReqDTO;
import org.rhw.bmr.project.dto.req.ShortLinkPageReqDTO;
import org.rhw.bmr.project.dto.req.ShortLinkUpdateReqDTO;
import org.rhw.bmr.project.dto.resp.ShortLinkCountQueryRespDTO;
import org.rhw.bmr.project.dto.resp.ShortLinkCreateRespDTO;
import org.rhw.bmr.project.dto.resp.ShortLinkPageRespDTO;

import java.util.List;

public interface ShortLinkService extends IService<ShortLinkDO> {

    /**
     * 创建短链接
     * @param requestParam  短链接参数
     * @return  短链接实体
     */
    ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam);

    /**
     * 分页查询短链接
     * @param requestParam 短链接gid
     * @return  短链接分页返回结果
     */
    IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPageReqDTO requestParam);

    /**
     *  修改短链接内容
     * @param requestParam 短链接参数
     */
    void updateShortLink(ShortLinkUpdateReqDTO requestParam);

    /**
     * 查询分组内的短链接数量
     * @param requestParam  gid数组
     * @return  短链接数量
     */
    List<ShortLinkCountQueryRespDTO> listGroupShortLinkCount(List<String> requestParam);

    /**
     * 短链接跳转
     * @param shortUri  短链接后缀
     * @param request   Http请求
     * @param response  Http相应
     */
   void restoreUrl(String shortUri, HttpServletRequest request, HttpServletResponse response);

    /**
     * 短链接统计
     *
     * @param fullShortUrl         完整短链接
     * @param gid                  分组标识
     * @param shortLinkStatsRecord 短链接统计实体参数
     */
    void shortLinkStats(String fullShortUrl, String gid, ShortLinkStatsRecordDTO shortLinkStatsRecord);

}

