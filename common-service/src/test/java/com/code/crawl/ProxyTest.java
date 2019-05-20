package com.code.crawl;

import com.code.common.crawl.BrowersUA;
import com.code.common.crawl.WebClient;
import com.code.common.crawl.WebRequest;
import com.code.common.crawl.WebResponse;
import com.code.common.proxyplugin.GxbProxyPlugin;
import com.code.common.proxy.TrialProxyPlugin;
import com.code.common.proxyplugin.JiGuangProxyPlugin;
import com.code.common.proxyplugin.ZhiMaProxyPlugin;
import org.junit.Test;

import java.io.IOException;

public class ProxyTest {
    @Test
    public void test1() {
        TrialProxyPlugin proxyPlugin = null;
//        proxyPlugin = new JiGuangProxyPlugin();
        proxyPlugin = new ZhiMaProxyPlugin();
//        proxyPlugin = new GxbProxyPlugin();

        System.out.println(proxyPlugin.getProxy());
    }
    @Test
    public void test4(){
//        System.out.println(BrowersUA.OPERA.randomUa());


        WebClient client = WebClient.buildDefaultClient();
//        System.out.println(client.getUser_Agent());
        WebResponse resp = null;
        try {
            resp = client.execute(new WebRequest("https://www.taobao.com/"));
            System.out.println(resp.getRespText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
