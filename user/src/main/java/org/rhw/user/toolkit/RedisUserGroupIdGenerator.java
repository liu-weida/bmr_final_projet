package org.rhw.user.toolkit;

import jakarta.annotation.PostConstruct;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisUserGroupIdGenerator {

    private final StringRedisTemplate stringRedisTemplate;
    private final RedissonClient redissonClient;

    private static final String USERGROUP_ID = "userGroup:id";
    private static final String INITIAL_VALUE = "1000000000";
    private static final Long INCREMENT_BY = 10000L;

    private static final String LOCK_KEY = "userGroup:lock";

    @Autowired
    public RedisUserGroupIdGenerator(StringRedisTemplate stringRedisTemplate, RedissonClient redissonClient) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redissonClient = redissonClient;
    }

    @PostConstruct
    private void init() {
        Boolean hasKey = stringRedisTemplate.hasKey(USERGROUP_ID);
        if (hasKey == null || !hasKey) {
            stringRedisTemplate.opsForValue().setIfAbsent(USERGROUP_ID, INITIAL_VALUE);
        }
    }

    /**
     * 获取并初始化号段（直接使用 Redisson 分布式锁）
     *
     * @return 当前号段起始值
     */
    public Long getAndIncrement() {
        // 获取分布式锁
        RLock lock = redissonClient.getLock(LOCK_KEY);

        // 阻塞等待获取锁
        lock.lock();

        try {
            Long newValue = stringRedisTemplate.opsForValue().increment(USERGROUP_ID, INCREMENT_BY);
            if (newValue == null) {
                throw new RuntimeException("Failed to increment the userGroup:id in Redis");
            }
            return newValue - INCREMENT_BY;
        } finally {
            lock.unlock(); // 释放锁
        }
    }
}
