package com.code.crawl;

import com.code.common.proxyplugin.GxbProxyPlugin;
import com.code.common.proxy.TrialProxyPlugin;
import org.junit.Test;

public class ProxyTest {
    @Test
    public void test1() {
        TrialProxyPlugin proxyPlugin = null;
//        proxyPlugin = new JiGuangProxyPlugin();
        proxyPlugin = new GxbProxyPlugin();

        System.out.println(proxyPlugin.getProxy());
    }
}
