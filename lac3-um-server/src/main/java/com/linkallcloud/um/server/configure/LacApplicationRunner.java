package com.linkallcloud.um.server.configure;

import java.util.Date;

import com.linkallcloud.um.service.queue.ILacQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.linkallcloud.um.server.service.queue.LacDbQueueExecutor;

@Component
@Order(1)
public class LacApplicationRunner implements ApplicationRunner {

    @Autowired
    private ILacQueueService lacQueueService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LacDbQueueExecutor ex = new LacDbQueueExecutor();
        ex.execute(lacQueueService);

        System.out.println("###################" + "异步队列运行中，now=" + new Date());
    }

}
