package com.linkallcloud.um.server.service.log.consumer;

import java.util.Date;
import java.util.List;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.linkallcloud.core.laclog.LacBusiLog;
import com.linkallcloud.log.core.constant.LogMessageConstant;
import com.linkallcloud.um.service.es.IEsService;

import cn.hutool.core.date.DateUtil;

@Component
public class LogPushService implements MessageListenerConcurrently {

    @Autowired
    private IEsService esService;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

        Multimap<String, String> ds = ArrayListMultimap.create();
        for (MessageExt msg : msgs) {
            LacBusiLog log = null;
            String body = null;
            try {
                body = new String(msg.getBody(), RemotingHelper.DEFAULT_CHARSET);
                log = JSON.parseObject(body, LacBusiLog.class);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            if (log != null) {
                if (log.getOperateTime() == null) {
                    log.setOperateTime(new Date());
                }
                String ikey = log.getAppName() + LogMessageConstant.UNDER_LINE
                        + DateUtil.format(log.getOperateTime(), LogMessageConstant.DATE_FORMAT_YYYYMMDD)
                        + LogMessageConstant.KEY_SEPARATOR + log.getAppType();

                ds.put(ikey, body);
            }
        }

        boolean b = esService.instertBulk(ds);
        if (!b) {
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
