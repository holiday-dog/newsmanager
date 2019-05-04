package com.code.crawl;

import com.code.common.crawl.PostInterceptor;
import com.code.common.crawl.PreInterceptor;
import com.code.common.crawl.WebClient;
import com.code.common.crawl.WebUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Test {
    //@org.junit.Test
    public void test1() throws IOException {

//        PreInterceptor p1 = new PreInterceptor() {
//
//            @Override
//            public void executeTask(HttpRequest httpRequest, HttpClientContext httpContext) {
//                System.out.println("pre1 task");
//            }
//        };
//        PreInterceptor p2 = new PreInterceptor() {
//            @Override
//            public void executeTask(HttpRequest httpRequest, HttpClientContext httpContext) {
//                System.out.println("pre2 task");
//            }
//        };
//        p1.addNext(p2);
//        PostInterceptor pp1 = new PostInterceptor() {
//            @Override
//            public void executeTask(HttpResponse httpResponse, HttpClientContext httpContext) {
//                System.out.println("post1 task");
//            }
//        };
//        PostInterceptor pp2 = new PostInterceptor() {
//            @Override
//            public void executeTask(HttpResponse httpResponse, HttpClientContext httpContext) {
//                System.out.println("post2 task");
//            }
//        };
//        pp1.addNext(pp2);
//        WebClient webClient = WebClient.create();
//        webClient.buildPreInterceptor(p1);
//        webClient.buildPostInterceptor(pp1);
//        webClient.build();
//        webClient.execute(null);
        WebClient client = WebUtils.multiThreadClient().build();
        try {
            TimeUnit.SECONDS.sleep(12);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        client.close();
        System.out.println("close");

        try {
            TimeUnit.MINUTES.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void tet(){
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
//将最大总连接数增加到200
        cm.setMaxTotal(200);
//将每条路由的默认最大连接数增加到20
        cm.setDefaultMaxPerRoute(20);
//增加localhost的最大连接数：80到50
        HttpHost localhost = new HttpHost("www.baidu.com");
        cm.setMaxPerRoute(new HttpRoute(localhost), 50);

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        System.out.println(httpClient.getConnectionManager().getClass());
    }
    //@org.junit.Test
    public void tet2(){
        Properties p = System.getProperties();

        for(Map.Entry<Object, Object> entry:p.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }

    //@org.junit.Test
    public void tet1() throws IOException {
       WebClient client = WebClient.defaultClient();
       client.execute(null);
    }
}
