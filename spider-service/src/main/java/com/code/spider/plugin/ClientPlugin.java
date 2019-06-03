package com.code.spider.plugin;

import com.alibaba.fastjson.JSON;
import com.code.common.bean.ProxyObj;
import com.code.common.crawl.WebClient;
import com.code.common.enums.ProcessStatus;
import com.code.common.exception.CodeException;
import com.code.common.proxy.ProxyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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

    abstract Map<String, Object> handleData(Map<String, Object> resultMap);

    public Map<String, Object> retryProcess(Map<String, Object> resultMap, WebClient client) throws IOException {
        return null;
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

            for (int i = 0; i < executorCount; i++) {
                try {
                    logger.info("retry {} times execute process method", i);
                    spiderData = process(resultMap);
                    break;
                } catch (Exception e) {
                    if (e instanceof CodeException) {
                        logger.error("happen spiderException, spider end.., error:{}", e);
                        return ProcessStatus.SPIDER_FAIL;
                    } else if (e instanceof ConnectException || e instanceof ConnectTimeoutException || e instanceof UnknownHostException) {
                        logger.error("request error, need proxy retry msg:{}", e);
                        ProxyObj obj = ProxyUtils.getProxy();
                        if (obj != null) {
                            WebClient client = WebClient.buildDefaultClient().buildRouteAndCount(50, 100).buildProxy(WebClient.ProxyType.PROXY_ADDRESS, obj.getProxyHost(), obj.getProxyPort()).build();
                            spiderData = retryProcess(resultMap, client);
                        }
                    }
                    logger.error("has error, msg:{}", e);
                }
            }
            logger.info("execute handleData method,body:{}", JSON.toJSONString(resultMap));
            resultMap.putAll(handleData(spiderData));

            logger.info("execute sendToMQ");
            if (StringUtils.isEmpty(sendToMQ(resultMap))) {

            }
        } catch (Exception e) {
            logger.error("happen exception, msg:{}", e);
        }

        logger.info("{} plugin spider success..", getClientPluginName());
        return ProcessStatus.SPIDER_SUCCESS;
    }

    private String sendToMQ(Map<String, Object> resultMap) {
        try {

        } catch (Exception e) {
            logger.error("");
        }
        return StringUtils.EMPTY;
    }

}
