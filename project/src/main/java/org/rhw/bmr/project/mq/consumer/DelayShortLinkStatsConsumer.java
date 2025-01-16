package org.rhw.bmr.project.mq.consumer;


import lombok.RequiredArgsConstructor;
import org.rhw.bmr.project.dto.biz.ShortLinkStatsRecordDTO;
import org.rhw.bmr.project.service.ShortLinkService;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

import static org.rhw.bmr.project.common.constant.RedisKeyConstant.DELAY_QUEUE_STATS_KEY;


/**
 * 延迟记录短链接统计组件
 */
@Component
@RequiredArgsConstructor
public class DelayShortLinkStatsConsumer implements InitializingBean {

    private final RedissonClient redissonClient;
    private final ShortLinkService shortLinkService;

    public void onMessage() {
        Executors.newSingleThreadExecutor(
                        runnable -> {
                            Thread thread = new Thread(runnable);
                            thread.setName("delay_short-link_stats_consumer");
                            // 设置为守护线程
                            thread.setDaemon(Boolean.TRUE);
                            return thread;
                        })
                .execute(() -> {
                    // 根据Key获取对应的专属延迟队列
                    RBlockingDeque<ShortLinkStatsRecordDTO> blockingDeque = redissonClient.getBlockingDeque(DELAY_QUEUE_STATS_KEY);
                    RDelayedQueue<ShortLinkStatsRecordDTO> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
                    for (; ; ) {
                        try {
                            // 获取到期的任务
                            ShortLinkStatsRecordDTO statsRecord = delayedQueue.poll();
                            if (statsRecord != null) {
                                // 执行统计
                                shortLinkService.shortLinkStats(null, null, statsRecord);
                                continue;
                            }
                            // 如果队列中没有到期任务，则线程使用 LockSupport.parkUntil(500) 暂停 500 毫秒，避免空循环。
                            LockSupport.parkUntil(System.currentTimeMillis() + 500);
                        } catch (Throwable ignored) {
                        }
                    }
                });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        onMessage();
    }
}