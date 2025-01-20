package org.rhw.bmr.user.common.biz.user;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.rhw.bmr.user.common.biz.user.UserContext;
import org.rhw.bmr.user.common.biz.user.UserInfoDTO;

/**
 * 用户信息传输过滤器
 * 提取用户信息（从请求头）。
 * 绑定用户信息到线程上下文。
 * 清理线程上下文，确保线程安全。
 * */
// 使用 Lombok 提供的注解，自动生成构造器，注入所有 `final` 修饰的字段
@RequiredArgsConstructor
// 实现 `Filter` 接口，用于拦截 HTTP 请求和响应
public class UserTransmitFilter implements Filter {

    /**
     * 过滤器的核心逻辑
     * 作用：从 HTTP 请求的头部信息中提取用户信息，并存储到线程上下文中，便于后续业务逻辑使用
     */
    @SneakyThrows // Lombok 注解，隐藏受检异常（Checked Exception）的显式声明
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        // 将通用的 ServletRequest 转换为 HttpServletRequest，以便访问 HTTP 专用方法
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        // 从请求头中获取 `username` 字段
        String username = httpServletRequest.getHeader("username");

        // 如果 `username` 不为空，则继续处理
        if (StrUtil.isNotBlank(username)) { // 使用 Hutool 工具类判断字符串是否不为空
            // 从请求头中获取 `userId` 和 `token` 字段
            String userId = httpServletRequest.getHeader("userId");
            String token = httpServletRequest.getHeader("token");

            // 构造用户信息对象（`UserInfoDTO`），包含用户 ID、用户名、真实姓名等信息
            UserInfoDTO userInfoDTO = new UserInfoDTO(userId, username, token);

            // 将用户信息存储到当前线程的上下文中（通过 `UserContext` 类）
            UserContext.setUser(userInfoDTO);
        }

        try {
            // 将请求传递给过滤器链中的下一个过滤器或最终的目标资源
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            // 在请求处理完成后，清除线程上下文中的用户信息，避免线程复用导致的数据泄漏
            UserContext.removeUser();
        }
    }
}
