package com.code.spider.plugin;

import com.code.common.enums.ProcessStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public abstract class ClientPlugin {
    private Logger logger = LoggerFactory.getLogger(ClientPlugin.class);
    private Integer executorCount = 2;

    private Boolean retry;

    String clientName;

    abstract void setName();

    public String getName() {
        return clientName;
    }

    public Integer getExecutorCount() {
        return executorCount;
    }

    public void setExecutorCount(Integer executorCount) {
        this.executorCount = executorCount;
    }

    public Boolean getRetry() {
        return retry;
    }

    public void setRetry(Boolean retry) {
        this.retry = retry;
    }

    abstract Map<String, Object> preProcess(Map<String, Object> params);

    abstract Map<String, Object> process(Map<String, Object> params);

    public ProcessStatus spiderProcess(Map<String, Object> args) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.putAll(args);
        logger.info("{} start spider..", getName());

        try {
            logger.info("execute preProcess method");
            resultMap = preProcess(resultMap);

            for (int i = 0; i < executorCount; i++) {
                try {
                    logger.info("retry {} times execute process method", i);
                    resultMap = process(resultMap);
                    logger.info("retry {} times execute success", i);
                    break;
                } catch (Exception e) {
                    if (e instanceof SpiderException) {
                        logger.error("happen spiderException, spider end..");
                        return ProcessStatus.SPIDER_FAIL;
                    }
                }
            }

            logger.info("execute sendToMQ");
            sendToMQ(resultMap);
        } catch (Exception e) {
            logger.error("happen exception, msg:{}", e);
        }

        logger.info("{} plugin spider success..", getName());
        return ProcessStatus.SPIDER_SUCCESS;
    }

    private String sendToMQ(Map<String, Object> resultMap) {
        return null;
    }
}
