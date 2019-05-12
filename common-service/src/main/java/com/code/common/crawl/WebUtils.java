package com.code.common.crawl;

import com.code.common.exception.CodeException;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class WebUtils {
    public static WebClient defaultClient() {
        return WebClient.buildDefaultClient();
    }

    public static WebClient multiThreadClient() {
        return multiThreadClient(20, 200);
    }

//    public static WebClient multiThreadClient(int countPerRoute, int maxCount) {
//        return multiThreadClient(countPerRoute, maxCount);
//    }

    public static WebClient multiThreadClient(int countPerRoute, int maxCount) {
        if (countPerRoute < 1 || maxCount < 1) {
            throw new CodeException("连接池管理器的maxTotal与perRoute小于0");
        }
        PoolingHttpClientConnectionManager connectionManager = createConnectionManager(countPerRoute, maxCount);

        WebClient client = WebClient.buildCustomeClient().buildConnectionManager(connectionManager).buildConnectionMonitor(new IdleConnectionMonitor(connectionManager));
        return client;
    }

    //创建连接池管理器，并注入注册信息
    private static PoolingHttpClientConnectionManager createConnectionManager(int countPerRoute, int maxCount) {
        ConnectionSocketFactory socketFactory = PlainConnectionSocketFactory.getSocketFactory();
        LayeredConnectionSocketFactory sslSocketFactory = SSLConnectionSocketFactory.getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("HTTP", socketFactory).register("HTTPS", sslSocketFactory).build();

        PoolingHttpClientConnectionManager connectionManager = null;
        connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setDefaultMaxPerRoute(countPerRoute);
        connectionManager.setMaxTotal(maxCount);
        return connectionManager;
    }

}
