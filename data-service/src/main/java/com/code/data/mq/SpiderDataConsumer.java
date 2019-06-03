package com.code.data.mq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SpiderDataConsumer {
    private static String nameAddr = "localhost:9876";
    private static String consumeGroup = "spider_consumer";
    private static String topic = "spiderdata";
    private static String tag = "rawdata";
    private static Logger logger = LoggerFactory.getLogger(SpiderDataConsumer.class);
    @Autowired
    private MessageListenerConcurrently messageListenerConcurrently;

    @PostConstruct
    public void consume() {
        try {
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumeGroup);
            //设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            //指定nameServer的地址
            consumer.setNamesrvAddr(nameAddr);
            //指定订阅的topic及tag表达式
            consumer.subscribe(topic, tag);
            consumer.registerMessageListener(messageListenerConcurrently);
            //启动消费者实例
            consumer.start();
            logger.info("data-service consume start success");
        } catch (Exception e) {
            logger.error("data-service consume start fail, msg:{}", e);
        }
    }
}
