package com.code.common.crawl;

import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

public class WebRetryHandler implements HttpRequestRetryHandler {
    private Integer retryCount = 3;

    @Override
    public boolean retryRequest(IOException e, int i, HttpContext httpContext) {
        if (i > retryCount) {
            return false;
        }
        if (e instanceof IOException) {
            return true;
        }
        if (e instanceof InterruptedIOException || e instanceof UnknownHostException || e instanceof ConnectTimeoutException || e instanceof SSLException) {
            //连接中断，主机为识别，连接超时，ssl握手失败
            return false;
        }
        return false;
    }
}
