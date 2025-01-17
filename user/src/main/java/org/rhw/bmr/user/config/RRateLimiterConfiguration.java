package org.rhw.bmr.user.config;


import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.rhw.bmr.user.common.constant.RedisRateLimiterConstant.LOCK_USER_REGISTER_RATE_KEY;


@Configuration
public class RRateLimiterConfiguration {
    /**
     * 限流器设置
     * 每秒最多200个令牌
     * OVERALL表示全局限流
     * @return RRateLimiter
     */
    @Bean
    public RRateLimiter userRegisterRateLimiter(RedissonClient redissonClient) {
       RRateLimiter rateLimiter = redissonClient.getRateLimiter(LOCK_USER_REGISTER_RATE_KEY);
        rateLimiter.trySetRate(RateType.OVERALL, 200, 1, RateIntervalUnit.SECONDS);
        return rateLimiter;
    }
}
