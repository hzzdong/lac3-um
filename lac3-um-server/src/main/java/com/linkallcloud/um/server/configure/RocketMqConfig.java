package com.linkallcloud.um.server.configure;

import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.linkallcloud.log.core.constant.LogMessageConstant;
import com.linkallcloud.um.server.service.log.consumer.LogPushService;

@Configuration
public class RocketMqConfig {

    @Value("${log.mq.namesrvAddr}")
    private String namesrvAddr;
    @Value("${log.mq.mqGroupName}")
    private String mqGroupName;
    @Value("${log.mq.accessKey}")
    private String accessKey;
    @Value("${log.mq.secretKey}")
    private String secretKey;

    @Autowired
    private LogPushService logPushService;

    @Bean(name = "logPushConsumer", destroyMethod = "shutdown", initMethod = "start")
    public DefaultMQPushConsumer missionPushConsumer() throws MQClientException {
        AclClientRPCHook ach = new AclClientRPCHook(new SessionCredentials(accessKey, secretKey));
        DefaultMQPushConsumer consumer =
                new DefaultMQPushConsumer(mqGroupName + "_consumer", ach, new AllocateMessageQueueAveragely());
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.setPollNameServerInterval(1000 * 5);
        consumer.setMessageModel(MessageModel.CLUSTERING);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer.subscribe(LogMessageConstant.LOG_TOPIC, "*");
        consumer.setConsumeMessageBatchMaxSize(1000);
        consumer.registerMessageListener(logPushService);
        return consumer;
    }

}
