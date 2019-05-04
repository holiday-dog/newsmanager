package com.code.common.crawl;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

public abstract class PreInterceptor implements HttpRequestInterceptor {
    private PreInterceptor interceptor;

    public abstract void executeTask(HttpRequest httpRequest, HttpClientContext httpContext);

    public void addNext(PreInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        executeTask(httpRequest, (HttpClientContext) httpContext);
        if (next() != null) {
            doNextTask(httpRequest, httpContext);
        }
    }

    private void doNextTask(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        next().process(httpRequest, httpContext);
    }

    PreInterceptor next() {
        return interceptor;
    }


}
