package com.code;

import com.code.common.utils.IOUtils;
import com.code.spider.config.SingleProducer;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;

public class MqTest {
    private String page;

    @Test
    public void test() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        DefaultMQProducer defaultMQProducer = SingleProducer.producer();
        System.out.println(page);

        defaultMQProducer.send(new Message("spiderdata", "rawdata", page.getBytes()));

//        SingleProducer.consumer();
    }

    @Before
    public void before() throws IOException {
        page = IOUtils.stringByResource("test/page.json", Charset.defaultCharset());
    }
}
