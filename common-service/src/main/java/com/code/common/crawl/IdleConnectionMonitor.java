package com.code.common.crawl;

import com.code.common.exception.CodeException;
import org.apache.http.conn.HttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class IdleConnectionMonitor extends Thread {
    private HttpClientConnectionManager connectionManager;

    private Boolean flag = true;

    private final static String THREAD_NAME = "idle_connection_monitor_thread";

    private Logger logger = LoggerFactory.getLogger(IdleConnectionMonitor.class);

    public IdleConnectionMonitor(HttpClientConnectionManager connectionManager) {
        super(THREAD_NAME);
        this.connectionManager = connectionManager;
    }

    @Override
    public void run() {
        logger.info("start monitor connection...");
        try {
            while (flag) {
                System.out.println(flag);
                logger.info("check..");
                synchronized (this) {
                    TimeUnit.SECONDS.sleep(5);
                    //关闭过期的连接
                    connectionManager.closeExpiredConnections();
                    //（可选）关闭连接
                    //闲置时间超过30秒
                    connectionManager.closeIdleConnections(2, TimeUnit.MINUTES);
                }
            }
        } catch (Exception e) {
            throw new CodeException("未活跃连接关闭异常", e);
        }
        logger.info("end monitor..");
    }
    public void close(){
        flag = false;
    }
}
