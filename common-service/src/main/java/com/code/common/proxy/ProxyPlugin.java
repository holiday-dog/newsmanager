package com.code.common.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class ProxyPlugin {
    private static Logger logger = LoggerFactory.getLogger(ProxyPlugin.class);

    public abstract String process();

    public abstract List<ProxyStr> resolveProxys(String page);

    public void doProcess() {
        try {
            String page = process();
            List<ProxyStr> proxyStrs= resolveProxys(page);
            putToRedis(proxyStrs);
        } catch (Exception e) {
            logger.error("请求代理出错, msg:{}", e);
        }
    }

    public void putToRedis(List<ProxyStr> proxyStrs){

    }
}
