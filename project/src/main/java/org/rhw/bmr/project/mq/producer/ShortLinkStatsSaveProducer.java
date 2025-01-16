package org.rhw.bmr.project.mq.producer;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 短链接监控状态保存消息队列生产者
 */
@Component
@RequiredArgsConstructor
public class ShortLinkStatsSaveProducer {

    private final StringRedisTemplate stringRedisTemplate;

    @Value("${spring.data.redis.channel-topic.short-link-stats}")
    private String topic;

    /**
     * 发送延迟消费短链接统计
     */
    // 使用 Redis Stream 的 add 方法，将统计数据写入指定的 topic
    public void send(Map<String, String> producerMap) {
        stringRedisTemplate.opsForStream().add(topic, producerMap);
    }
}