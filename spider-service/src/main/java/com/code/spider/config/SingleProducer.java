package com.code.spider.config;

import com.code.common.utils.RandomUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SingleProducer {
    private static Logger logger = LoggerFactory.getLogger(SingleProducer.class);
    private static String nameAddr = "localhost:9876";
    private static String produceGroup = "spider_producer";
    public static String topic = "spiderdata";
    public static String tag = "rawdata";

    public static DefaultMQProducer producer() {
        DefaultMQProducer producer = null;
        try {
            producer = new DefaultMQProducer(produceGroup);
            producer.setNamesrvAddr(nameAddr);
            producer.setInstanceName(RandomUtils.randomInstanceName("producer-"));

            producer.start();
            logger.info("spider-producer init success");
        } catch (Exception e) {
            logger.info("spider-producer init error:{}", e);
        }
        return producer;
    }

    public static DefaultMQPushConsumer consumer() throws MQClientException, InterruptedException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("ConsumerGroupName");
        //设置Consumer第一次启动是从队列头部开始消费还是队列尾部开始消费<br>
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        //指定nameServer的地址
        consumer.setNamesrvAddr(nameAddr);
        //指定订阅的topic及tag表达式
        consumer.subscribe("spiderdata", "*");

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                MessageExt messageExt = msgs.get(0);
                System.out.println(String.format("Custome message [%s],tagName[%s]",
                        new String(messageExt.getBody()),
                        messageExt.getTags()));
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //启动消费者实例
        consumer.start();

        TimeUnit.MINUTES.sleep(2);
        return consumer;
    }
}
