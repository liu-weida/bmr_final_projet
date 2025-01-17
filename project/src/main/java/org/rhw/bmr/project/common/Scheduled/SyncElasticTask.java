package org.rhw.bmr.project.common.Scheduled;


import lombok.extern.slf4j.Slf4j;
import org.rhw.bmr.project.service.BookSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SyncElasticTask implements SchedulingConfigurer {

    @Value("${scheduledTaskSyncElastic.enabled}")
    private boolean enabled;

    @Value("${scheduledTaskSyncElastic.interval}")
    private long interval;

    @Autowired
    private BookSyncService bookSyncService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        if (enabled) {
            taskRegistrar.addFixedRateTask(() -> {
                bookSyncService.syncBooksToElasticsearch();
            }, interval);
        }
    }
}

