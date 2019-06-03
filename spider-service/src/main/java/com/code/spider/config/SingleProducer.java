package com.code.spider.config;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingleProducer {
    private static Logger logger = LoggerFactory.getLogger(SingleProducer.class);
    private static String nameAddr = "localhost:9876";
    private static String produceGroup = "spider_producer";

    public static DefaultMQProducer producer() {
        DefaultMQProducer producer = null;
        try {
            producer = new DefaultMQProducer(produceGroup);
            producer.setNamesrvAddr(nameAddr);

            producer.start();
            logger.info("spider-producer init success");
        } catch (Exception e) {
            logger.info("spider-producer init error:{}", e);
        }
        return producer;
    }
}
