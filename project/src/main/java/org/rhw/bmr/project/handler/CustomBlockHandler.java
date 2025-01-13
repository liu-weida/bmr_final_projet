package org.rhw.bmr.project.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.rhw.bmr.project.common.convention.result.Result;
import org.rhw.bmr.project.dto.req.ShortLinkCreateReqDTO;
import org.rhw.bmr.project.dto.resp.ShortLinkCreateRespDTO;


/**
 * 自定义流控策略
 */
public class CustomBlockHandler {

    public static Result<ShortLinkCreateRespDTO> createShortLinkBlockHandlerMethod(ShortLinkCreateReqDTO requestParam, BlockException exception) {
        return new Result<ShortLinkCreateRespDTO>().setCode("B100000").setMessage("当前访问网站人数过多，请稍后再试...");
    }
}
