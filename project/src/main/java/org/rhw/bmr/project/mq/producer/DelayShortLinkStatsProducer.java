package org.rhw.bmr.project.mq.producer;


import lombok.RequiredArgsConstructor;
import org.rhw.bmr.project.dto.biz.ShortLinkStatsRecordDTO;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static org.rhw.bmr.project.common.constant.RedisKeyConstant.DELAY_QUEUE_STATS_KEY;


/**
 * 延迟消费短链接统计发送者
 */
@Component
@RequiredArgsConstructor
public class DelayShortLinkStatsProducer {

    private final RedissonClient redissonClient;

    /**
     * 发送延迟消费短链接统计
     *
     * @param statsRecord 短链接统计实体参数
     */
    public void send(ShortLinkStatsRecordDTO statsRecord) {
        // 获取阻塞队列标识
        RBlockingDeque<ShortLinkStatsRecordDTO> blockingDeque = redissonClient.getBlockingDeque(DELAY_QUEUE_STATS_KEY);
        // 将 RBlockingDeque 包装为支持延迟功能的 RDelayedQueue。
        RDelayedQueue<ShortLinkStatsRecordDTO> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
        // 使用 offer 方法将任务 statsRecord 放入队列，并设置延迟时间为 5 秒。
        delayedQueue.offer(statsRecord, 5, TimeUnit.SECONDS);
    }
}