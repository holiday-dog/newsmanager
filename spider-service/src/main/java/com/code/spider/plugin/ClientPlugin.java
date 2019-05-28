package com.code.spider.plugin;

import com.code.common.enums.ProcessStatus;
import com.code.common.exception.CodeException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    abstract Map<String, Object> handleData(Map<String, Object> resultMap);

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

            for (int i = 0; i < executorCount; i++) {
                try {
                    logger.info("retry {} times execute process method", i);
                    spiderData = process(resultMap);
                    break;
                } catch (Exception e) {
                    if (e instanceof CodeException) {
                        logger.error("happen spiderException, spider end..");
                        return ProcessStatus.SPIDER_FAIL;
                    }
                }
            }
            logger.info("execute handleData method");
            resultMap.putAll(handleData(spiderData));

            logger.info("execute sendToMQ");
            sendToMQ(resultMap);
        } catch (Exception e) {
            logger.error("happen exception, msg:{}", e);
        }

        logger.info("{} plugin spider success..", getClientPluginName());
        return ProcessStatus.SPIDER_SUCCESS;
    }

    private String sendToMQ(Map<String, Object> resultMap) {
        return null;
    }

}
