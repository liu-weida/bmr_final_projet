package org.rhw.bmr.project.common.scheduled;


import lombok.extern.slf4j.Slf4j;
import org.rhw.bmr.project.service.BookSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class SyncElasticTask implements SchedulingConfigurer {

    @Value("${scheduledTaskSyncElastic.enabled}")
    private boolean enabled;

    @Value("${scheduledTaskSyncElastic.interval}")
    private long interval;

    @Value("${scheduledTaskSyncElastic.start}")
    private long start;

    @Autowired
    private BookSyncService bookSyncService;


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        if (enabled) {
            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

            // 延迟 60 秒后开始执行任务
            executorService.scheduleAtFixedRate(() -> {
                try {
                    bookSyncService.syncBooksToElasticsearch();
                } catch (Exception e) {
                    log.error("执行书籍插入任务时发生错误: ", e);
                }
            }, start, interval, TimeUnit.MILLISECONDS);
        }
    }

}

