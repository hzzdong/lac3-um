package com.linkallcloud.um.server;

import com.linkallcloud.um.service.party.IYwUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.linkallcloud.core.dto.Trace;
import com.linkallcloud.um.domain.party.YwUser;

@SpringBootTest
@RunWith(SpringRunner.class)
public class YwUserServiceTest {

    @Autowired
    private IYwUserService ywUserService;

    @Test
    public void cachetest() throws Exception {
        Trace trace = new Trace("20000");
        YwUser user = ywUserService.fetchById(trace, 2L);

        //YwUser user2 = ywUserService.fecthByAccount(trace, "zhoudong");

        user.setRemark(user.getRemark() + 1);
        trace.addKey("ID-2");
        trace.addKey("Account-zhoudong");
        ywUserService.update(trace, user);

        user = ywUserService.fetchById(trace, 2L);

        user.setRemark(user.getRemark() + 1);
    }

}
