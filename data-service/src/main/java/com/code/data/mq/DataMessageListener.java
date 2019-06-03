package com.code.data.mq;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataMessageListener implements MessageListenerConcurrently {
    @Autowired
    private SpiderDataHandler spiderDataHandler;
    private static Integer consumeCount = 2;
    private static Logger logger = LoggerFactory.getLogger(DataMessageListener.class);

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        MessageExt messageExt = list.get(0);
        if (messageExt != null) {
            String body = new String(messageExt.getBody());
            for (int i = 0; i < consumeCount; i++) {
                try {
                    boolean flag = spiderDataHandler.handler(body);
                    if (flag) {
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                } catch (Exception e) {
                    logger.error("consume fail, retry {} times", i + 1);
                }
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
