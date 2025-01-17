package org.rhw.bmr.user.remote;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.rhw.bmr.user.remote.dto.req.*;
import org.rhw.bmr.user.remote.dto.resp.ShortLinkCountQueryRespDTO;
import org.rhw.bmr.user.remote.dto.resp.ShortLinkCreateRespDTO;
import org.rhw.bmr.user.remote.dto.resp.ShortLinkPageRespDTO;
import org.rhw.bmr.user.common.convention.result.Result;
import org.rhw.bmr.user.dto.req.BmrRecycleBinPageReqDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 短链接中台远程调用服务
 */
public interface ShortLinkRemoteService {

    default Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam){
        Map<String, Object>requestMap = new HashMap<>();
        requestMap.put("gid", requestParam.getGid());
        requestMap.put("current", requestParam.getCurrent());
        requestMap.put("size", requestParam.getSize());
        String resultPageStr = HttpUtil.get(
                "http://127.0.0.1:8001/api/short-link/project/v1/page",
                requestMap);

        return JSON.parseObject(resultPageStr, new TypeReference<>(){});
    }

    default Result<ShortLinkCreateRespDTO> createShortLink(ShortLinkCreateReqDTO requestParam){
        String resultBodyStr = HttpUtil.post(
                "http://127.0.0.1:8001/api/short-link/project/v1/create",
                JSON.toJSONString(requestParam));


        return JSON.parseObject(resultBodyStr, new TypeReference<>(){});
    }


    default void updateShortLink(ShortLinkUpdateReqDTO requestParam){
        HttpUtil.post("http://127.0.0.1:8001/api/short-link/project/v1/update",
                JSON.toJSONString(requestParam));

    }

    /**
     * 查询短链接数量
     */
    default Result<List<ShortLinkCountQueryRespDTO>> listGroupShortLinkCount(List<String> requestParam){
        Map<String, Object>requestMap = new HashMap<>();
        requestMap.put("requestParam", requestParam);
        String resultPageStr = HttpUtil.get(
                "http://127.0.0.1:8001/api/short-link/project/v1/count",
                requestMap);

        return JSON.parseObject(resultPageStr, new TypeReference<>(){});
    }

    default Result<String> getTitleByUrl(@RequestParam("url") String url){
        String resultPageStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/project/v1/title?url=" + url);
        return JSON.parseObject(resultPageStr, new TypeReference<>(){});
    }

    default void saveRecycleBin(RecycleBinSaveReqDTO requestParam){
        HttpUtil.post("http://127.0.0.1:8001/api/short-link/project/v1/recycle-bin/save", JSON.toJSONString(requestParam));
    }

    /**
     * 分页查询回收站短链接
     */
    default Result<IPage<ShortLinkPageRespDTO>> pageRecycleBinShortLink(BmrRecycleBinPageReqDTO requestParam){
        Map<String, Object>requestMap = new HashMap<>();
        requestMap.put("gidList", requestParam.getGidList());
        requestMap.put("current", requestParam.getCurrent());
        requestMap.put("size", requestParam.getSize());
        String resultPageStr = HttpUtil.get(
                "http://127.0.0.1:8001/api/short-link/project/v1/recycle-bin/page",
                requestMap);

        return JSON.parseObject(resultPageStr, new TypeReference<>(){});
    }


    default void recoverRecycleBin(RecycleBinRecoverReqDTO requestParam){
        HttpUtil.post("http://127.0.0.1:8001/api/short-link/project/v1/recycle-bin/recover", JSON.toJSONString(requestParam));
    }

    default void removeRecycleBin(RecycleBinRemoveReqDTO requestParam){
        HttpUtil.post("http://127.0.0.1:8001/api/short-link/project/v1/recycle-bin/remove", JSON.toJSONString(requestParam));
    }


    default Result<ShortLinkStatsRespDTO> oneShortLinkStats(ShortLinkStatsReqDTO requestParam) {
        String resultBodyStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/project/v1/stats", BeanUtil.beanToMap(requestParam));
        return JSON.parseObject(resultBodyStr, new TypeReference<>(){});
    }


//    default Result<IPage<ShortLinkStatsAccessRecordRespDTO>> shortLinkStatsAccessRecord(ShortLinkStatsAccessRecordReqDTO requestParam) {
//        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(requestParam, false, true);
//        stringObjectMap.remove("orders");
//        stringObjectMap.remove("records");
//        String resultBodyStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/project/v1/stats/access-record", stringObjectMap);
//        return JSON.parseObject(resultBodyStr, new TypeReference<>(){});
//    }
}
