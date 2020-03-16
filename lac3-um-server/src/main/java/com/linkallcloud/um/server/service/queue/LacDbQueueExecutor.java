package com.linkallcloud.um.server.service.queue;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import com.linkallcloud.um.service.queue.ILacQueueService;

public class LacDbQueueExecutor {

    private static Log log = Logs.get();

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public void execute(ILacQueueService lacQueueService) {
        // 每一分钟执行一次
        executor.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                try {
                    lacQueueService.dealBusiness();
                } catch (Throwable e) {
                    log.error(e.getMessage(), e);
                }
            }

        }, 0, 10, TimeUnit.MINUTES);
    }

}
