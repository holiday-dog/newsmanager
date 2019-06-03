package com.code.spider.config;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:rocketmq.properties")
public class SpiderMqConfiguration {

    private Logger logger = LoggerFactory.getLogger(SpiderMqConfiguration.class);

    @Value("${rocketmq.nameaddr}")
    private String nameaddr;

    @Value("${rocketmq.produce.group}")
    private String produceGroup;

    @Value("${rocketmq.produce.topic}")
    private String produceTopic;

    @Value("${rocketmq.produce.tag}")
    private String produceTag;

    @Value("${rocketmq.consume.group}")
    private String consumeGroup;

//    @Bean
//    public DefaultMQProducer producer() throws MQClientException {
//        DefaultMQProducer producer = null;
//        try {
//            producer = new DefaultMQProducer(produceGroup);
//            producer.setNamesrvAddr(nameaddr);
//
//            producer.start();
//            logger.info("spider-producer init success");
//        } catch (Exception e) {
//            logger.info("spider-producer init error:{}", e);
//        }
//        return producer;
//    }

    @Bean
    public String spiderTopic() {
        return produceTopic;
    }

    @Bean
    public String spiderTag() {
        return produceTag;
    }

//    @Bean
//    public DefaultMQPushConsumer consumer() throws MQClientException {
//        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumeGroup);
//        consumer.setNamesrvAddr(nameaddr);
////        consumer.start();
//        return consumer;
//    }
}
