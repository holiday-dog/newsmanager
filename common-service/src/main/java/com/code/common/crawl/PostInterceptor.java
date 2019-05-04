package com.code.common.crawl;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

public abstract class PostInterceptor implements HttpResponseInterceptor {
    private PostInterceptor interceptor;

    public abstract void executeTask(HttpResponse httpResponse, HttpClientContext httpContext);

    @Override
    public void process(HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        executeTask(httpResponse, (HttpClientContext) httpContext);
        if (next() != null) {
            doNextTask(httpResponse, httpContext);
        }
    }

    private void doNextTask(HttpResponse httpResponse, HttpContext httpContext) throws IOException, HttpException {
        next().process(httpResponse, httpContext);
    }

    public void addNext(PostInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public PostInterceptor next() {
        return interceptor;
    }
}
