package org.rhw.bmr.user.remote;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.rhw.bmr.user.common.convention.result.Result;
import org.rhw.bmr.user.dto.req.BmrRecycleBinPageReqDTO;
import org.rhw.bmr.user.remote.dto.req.*;
import org.rhw.bmr.user.remote.dto.resp.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 短链接中台远程调用服务
 */
public interface BookSearchRemoteService {

    default Result<IPage<BookSearchRespDTO>> bookSearch(BookSearchReqDTO requestParam){
        Map<String, Object>requestMap = new HashMap<>();

        requestMap.put("title", requestParam.getTitle());
        requestMap.put("author", requestParam.getAuthor());
        requestMap.put("category", requestParam.getCategory());
        requestMap.put("language", requestParam.getLanguage());


        String resultPageStr = HttpUtil.get(
                "http://127.0.0.1:8001/api/bmr/project/v1/bookSearch_page",
                requestMap);
        return JSON.parseObject(resultPageStr, new TypeReference<>(){});
    }

    default Result<IPage<BookSearchByWordRespDTO>> bookSearchByWord(BookSearchByWordReqDTO requestParam){
        Map<String, Object>requestMap = new HashMap<>();

        requestMap.put("word", requestParam.getWord());

        String resultBodyStr = HttpUtil.get(
                "http://127.0.0.1:8001/api/bmr/project/v1/bookSearch_by_word",
                requestMap);

        return JSON.parseObject(resultBodyStr, new TypeReference<>(){});
    }


    default Result<IPage<BookSearchByRegespRespDTO>> bookSearchByRegexp(BookSearchByRegexpReqDTO requestParam){
        Map<String, Object>requestMap = new HashMap<>();

        requestMap.put("regularExpr", requestParam.getRegularExpr());

        String resultBodyStr = HttpUtil.get(
                "http://127.0.0.1:8001/api/bmr/project/v1/bookSearch_by_regexp",
                requestMap);

        return JSON.parseObject(resultBodyStr, new TypeReference<>(){});
    }

}
