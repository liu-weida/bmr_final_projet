package org.rhw.user.config;

import org.rhw.user.common.biz.user.UserFlowRiskControlFilter;
import org.rhw.user.common.biz.user.UserTransmitFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 用户配置自动装配
 * */
// 声明为 Spring 配置类，负责定义 Bean 并管理其生命周期
@Configuration
public class UserConfiguration {

    /**
     * 配置用户信息传递过滤器（UserTransmitFilter）
     */
    @Bean // 将此方法返回的对象注册为 Spring 容器中的 Bean
    public FilterRegistrationBean<UserTransmitFilter> globalUserTransmitFilter() {
        // 创建 FilterRegistrationBean，用于配置过滤器的注册信息
        FilterRegistrationBean<UserTransmitFilter> registration = new FilterRegistrationBean<>();

        // 设置过滤器实例（UserTransmitFilter）
        registration.setFilter(new UserTransmitFilter());

        // 设置过滤器应用的 URL 模式，这里设置为拦截所有请求
        registration.addUrlPatterns("/*");

        // 设置过滤器的执行顺序，数值越小优先级越高
        registration.setOrder(0);

        // 返回配置好的注册对象
        return registration;
    }

    /**
     * 配置用户操作流量风控过滤器（UserFlowRiskControlFilter）
     * 该过滤器仅在配置属性 `short-link.flow-limit.enable` 为 `true` 时启用
     */
    @Bean
    @ConditionalOnProperty(name = "bmr-user.flow-limit.enable", havingValue = "true") // 条件注解，只有满足条件时才注册此 Bean
    public FilterRegistrationBean<UserFlowRiskControlFilter> globalUserFlowRiskControlFilter(
            StringRedisTemplate stringRedisTemplate, // 注入 Redis 模板，用于流量控制的计数和限流
            UserFlowRiskControlConfiguration userFlowRiskControlConfiguration // 注入用户流量风控配置
    ) {
        // 创建 FilterRegistrationBean，用于配置过滤器的注册信息
        FilterRegistrationBean<UserFlowRiskControlFilter> registration = new FilterRegistrationBean<>();

        // 设置过滤器实例，并传入需要的依赖
        registration.setFilter(new UserFlowRiskControlFilter(stringRedisTemplate, userFlowRiskControlConfiguration));

        // 设置过滤器应用的 URL 模式，这里设置为拦截所有请求
        registration.addUrlPatterns("/*");

        // 设置过滤器的执行顺序，数值越小优先级越高（比用户信息传递过滤器优先级低）
        registration.setOrder(10);

        // 返回配置好的注册对象
        return registration;
    }
}
