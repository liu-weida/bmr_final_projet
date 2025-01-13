package org.rhw.bmr.user.common.biz.user;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.rhw.bmr.user.common.convention.exception.ClientException;
import org.rhw.bmr.user.common.convention.result.Results;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

import static org.rhw.bmr.user.common.enums.UserErrorCodeEnum.USER_TOKEN_FAIL;

/**
 * 用户信息传输过滤器
 */
@RequiredArgsConstructor
public class UserTransmitFilter implements Filter {

    private final StringRedisTemplate stringRedisTemplate;

    private static final List<String> IGNORE_URI = Lists.newArrayList(
            "/api/bmr/user/v1/user/login",
            "/api/bmr/user/v1/user/has-username",
            "/api/bmr/user/v1/title"
    );

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        String requestURI = httpServletRequest.getRequestURI();

        if (!IGNORE_URI.contains(requestURI)) {

            String method = httpServletRequest.getMethod();
            if (!(Objects.equals(method, "POST") && Objects.equals(requestURI, "/api/bmr/user/v1/user"))  ) {
                String username = httpServletRequest.getHeader("username");
                String token = httpServletRequest.getHeader("token");

                // 如果拿不到token或者说用户名不存在
                if (!StrUtil.isAllNotBlank(username, token)){
                    returnJson((HttpServletResponse)servletResponse, JSON.toJSONString(Results.failure(new ClientException(USER_TOKEN_FAIL))));
                    return;
                }

                Object userInfoJsonStr;
                try{
                    userInfoJsonStr = stringRedisTemplate.opsForHash().get("login_" + username, token);

                    if (userInfoJsonStr == null){
                        throw new ClientException(USER_TOKEN_FAIL);
                    }

                }catch(Exception e){
                    returnJson((HttpServletResponse)servletResponse, JSON.toJSONString(Results.failure(new ClientException(USER_TOKEN_FAIL))));
                    return;
                }

                UserInfoDTO userInfoDTO = JSON.parseObject(userInfoJsonStr.toString(), UserInfoDTO.class);
                UserContext.setUser(userInfoDTO);
            }
        }
        try {
             filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            UserContext.removeUser();
        }
    }

    private void returnJson(HttpServletResponse response, String json) throws Exception {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try{
            writer = response.getWriter();
            writer.write(json);
        }catch(IOException e){}
        finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}