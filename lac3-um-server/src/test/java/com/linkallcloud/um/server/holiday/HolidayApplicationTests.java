package com.linkallcloud.um.server.holiday;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.linkallcloud.um.server.configure.HolidayScheduledService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HolidayApplicationTests {
	
    @Autowired
    private HolidayScheduledService scheduledService;

    @Test
    public void contextLoads() {
    }

    /**
     * 首次部署最好运行此方法，生成当年的节假日信息
     */
    @Test
    public void init(){
        scheduledService.getWorkDay();
    }
}
