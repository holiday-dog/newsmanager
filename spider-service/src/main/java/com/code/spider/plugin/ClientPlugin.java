package com.code.spider.plugin;

import com.alibaba.fastjson.JSON;
import com.code.common.bean.ProxyObj;
import com.code.common.crawl.WebClient;
import com.code.common.enums.ProcessStatus;
import com.code.common.exception.CodeException;
import com.code.common.proxy.ProxyUtils;
import com.code.spider.bean.Constants;
import com.code.spider.config.SingleProducer;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public abstract class ClientPlugin {
    private Logger logger = LoggerFactory.getLogger(ClientPlugin.class);
    private Integer executorCount = 1;

    public abstract String getClientPluginName();

    public Integer getExecutorCount() {
        return executorCount;
    }

    public void setExecutorCount(Integer executorCount) {
        this.executorCount = executorCount;
    }

    public Boolean getRetry() {
        return false;
    }

    abstract Map<String, Object> preProcess(Map<String, Object> resultMap);

    abstract Map<String, Object> process(Map<String, Object> resultMap) throws Exception;

    abstract Map<String, Object> handleData(Map<String, Object> resultMap) throws Exception;

    public void retrySetClient(WebClient client) {
        return;
    }

    public Map<String, Object> postProcess(Map<String, Object> resultMap) {
        return resultMap;
    }

    public ProcessStatus spiderProcess(Map<String, Object> params) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, Object> spiderData = null;
        if (params != null) {
            resultMap.putAll(params);
        }
        logger.info("{} start spider..", getClientPluginName());

        try {
            logger.info("execute preProcess method");
            resultMap = preProcess(resultMap);

            int i = 0;
            for (; i < executorCount; i++) {
                try {
                    logger.info("retry {} times execute process method", i);
                    spiderData = process(resultMap);
                    resultMap.put(Constants.STAGE, ProcessStatus.SPIDER_SUCCESS);
                    break;
                } catch (Exception e) {
                    if (e instanceof CodeException) {
                        setStatus(resultMap, ProcessStatus.SPIDER_FAIL, e);
                        logger.error("happen spiderException, spider end.., error:{}", e);
                        return ProcessStatus.SPIDER_FAIL;
                    } else if (e instanceof ConnectException || e instanceof ConnectTimeoutException || e instanceof UnknownHostException) {
                        logger.error("request error, need proxy retry msg:{}", e);
                        ProxyObj obj = ProxyUtils.getProxy();
                        if (obj != null) {
                            WebClient client = WebClient.buildDefaultClient().buildRouteAndCount(50, 100).buildProxy(WebClient.ProxyType.PROXY_ADDRESS, obj.getProxyHost(), obj.getProxyPort()).build();
                            retrySetClient(client);
                            continue;
                        }
                        setStatus(resultMap, ProcessStatus.SPIDER_FAIL, e);
                    } else {
                        logger.error("has error, msg:{}", e);
                        setStatus(resultMap, ProcessStatus.SPIDER_FAIL, e);
                    }
                }
            }
            logger.info("rawdata:{}", JSON.toJSONString(spiderData));
            if (i == 2 || (ProcessStatus) resultMap.get(Constants.STAGE) != ProcessStatus.SPIDER_SUCCESS) {
                sendToMQ(resultMap);
                return (ProcessStatus) resultMap.get(Constants.STAGE);
            }
            try {
                logger.info("execute handleData method");
                resultMap.putAll(handleData(spiderData));
                setStatus(resultMap, ProcessStatus.HANDLE_SUCCESS, null);
            } catch (Exception e) {
                setStatus(resultMap, ProcessStatus.HANDLE_FAIL, e);
            }
            postProcess(resultMap);

            logger.info("{}", JSON.toJSONString(resultMap));
            logger.info("execute sendToMQ");
            String errorMsg = sendToMQ(resultMap);
            if (StringUtils.isEmpty(errorMsg)) {
                return ProcessStatus.SPIDER_SUCCESS;
            }
        } catch (Exception e) {
            logger.error("happen exception, msg:{}", e);
        }

        logger.info("{} plugin spider success..", getClientPluginName());
        return ProcessStatus.SPIDER_FAIL;
    }

    private String sendToMQ(Map<String, Object> resultMap) {
        try {
            SingleProducer.producer().send(new Message(SingleProducer.topic, SingleProducer.tag, JSON.toJSONBytes(resultMap)));
        } catch (Exception e) {
            logger.error("send error, msg:{}", e);
            return e.toString();
        }
        return StringUtils.EMPTY;
    }

    private void setStatus(Map<String, Object> resultMap, ProcessStatus status, Exception remark) {
        resultMap.put(Constants.STAGE, status.getVal());
        if (remark != null) {
            resultMap.put(Constants.PROCESS_REMARK, remark.toString());
        }
    }
}