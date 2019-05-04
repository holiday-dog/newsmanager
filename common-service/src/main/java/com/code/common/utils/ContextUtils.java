package com.code.common.utils;

import org.apache.http.client.protocol.HttpClientContext;

public class ContextUtils {
    private static ThreadLocal<HttpClientContext> crawlContext = new ThreadLocal<>();

    public static HttpClientContext getHttpContext() {
        return crawlContext.get();
    }

    public static void setHttpContext(HttpClientContext httpContext) {
        crawlContext.set(httpContext);
    }
}
