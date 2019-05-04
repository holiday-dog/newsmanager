package com.code;

import com.alibaba.fastjson.JSON;
import com.code.spider.SpiderApplication;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.TimeUnit;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SpiderApplication.class})
public class test {
    private Logger logger = LoggerFactory.getLogger(test.class);
    @Autowired
    DefaultMQProducer producer;

//    @Autowired
//    DefaultMQPushConsumer consumer;

    @Test
    public void test2() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {

        Message message = new Message("spiderdata", "tag1", "hello".getBytes());
        producer.send(message);
//        test1();
        producer.shutdown();
        logger.info("finish..");
    }
//    @Test
//    public void test1() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
//        System.out.println(producer);
//        System.out.println(consumer);
//
//
//        consumer.subscribe("spiderdata", "*");
//        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
//        consumer.registerMessageListener(new MessageListenerConcurrently() {
//
//            @Override
//            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
//                System.out.println(list);
//                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//            }
//        });
//        consumer.start();
//        TimeUnit.SECONDS.sleep(20);
//        consumer.shutdown();
//
////
//    }

}
