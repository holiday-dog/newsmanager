package com.code.common.crawl;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.message.BasicHeader;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ProxySelector;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WebClient {
    private final static String User_Agent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:63.0) Gecko/20100101 Firefox/63.0";
    private final static String Accept = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
    private final static String Accept_Language = "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2";
    private final static String Accept_Encoding = "gzip, deflate, br";
    private final static String Connection = "keep-alive";
    private final static String Pragma = "no-cache";
    private final static String Cache_Control = "no-cache";

    //线程安全
    private static HttpClient client = null;

    //httppost,httpget的父类
//    private HttpRequestBase request;

    //自定义返回结果处理
    private ResponseHandler handler;

    private HttpClientContext context;

    private Boolean needContext;

    private HttpClientBuilder clientBuilder;

    private IdleConnectionMonitor connectionMonitor;

    private RequestConfig config;

    private HttpHost proxy;

    private Boolean needCacheStream = false;

    private WebClient() {
    }

    public static WebClient buildDefaultClient() {
        WebClient webClient = new WebClient();
        webClient.client = HttpClients.createDefault();
        return webClient;
    }

    public static WebClient createCustome() {
        WebClient webClient = new WebClient();
        return webClient.init();
    }

    public WebClient init() {
        clientBuilder = HttpClients.custom();
        return this;
    }

    public WebClient build() {
        clientBuilder.setDefaultHeaders(buildDefaultHeaders());
        client = clientBuilder.build();
        return this;
    }

    //自定义实现httpclient
    public void customeClient(HttpClient client) {
        this.client = client;
    }

    public WebClient buildContext() {
        context = HttpClientContext.create();
        return this;
    }

    public WebClient buildNeedStreamCache() {
        needCacheStream = true;
        return this;
    }

    public WebClient buildPreInterceptor(PreInterceptor interceptor) {
        clientBuilder.addInterceptorFirst(interceptor);
        return this;
    }

    public WebClient buildPostInterceptor(PostInterceptor interceptor) {
        clientBuilder.addInterceptorLast(interceptor);
        return this;
    }

    public WebClient buildRetryHandler(HttpRequestRetryHandler retryHandler) {
        clientBuilder.setRetryHandler(retryHandler);
        return this;
    }

    public WebClient buildConnectionManager(HttpClientConnectionManager connectionManager) {
        clientBuilder.setConnectionManager(connectionManager);
        return this;
    }

    public static void setMaxCountPerRoute(HttpRoute route, int count) {
        ((PoolingHttpClientConnectionManager) client.getConnectionManager()).setMaxPerRoute(route, count);
    }

    public WebClient buildConnectionMonitor(IdleConnectionMonitor connectionMonitor) {
        this.connectionMonitor = connectionMonitor;
        connectionMonitor.start();
        return this;
    }

    public WebClient buildRequestConfig(RequestConfig config) {
        RequestConfig.Builder configBuilder = RequestConfig.custom();
        configBuilder.setCookieSpec(config.getCookieSpec());
        if (StringUtils.isEmpty(config.getCookieSpec())) {
            configBuilder.setCookieSpec(CookieSpecs.STANDARD);
        }
        this.config = configBuilder.build();
        clientBuilder.setDefaultRequestConfig(config);
        return this;
    }

    public WebClient buildProxy(ProxyType proxyType, String proxyHost, Integer proxyPort) {
        switch (proxyType.getVal()) {
            case "address":
                proxy = new HttpHost(proxyHost, proxyPort);
                DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
                clientBuilder.setRoutePlanner(routePlanner);
                break;
            case "jdk":
                SystemDefaultRoutePlanner systemDefaultRoutePlanner = new SystemDefaultRoutePlanner(ProxySelector.getDefault());
                clientBuilder.setRoutePlanner(systemDefaultRoutePlanner);
                break;
            case "none":
            default:
        }
        return this;
    }

    //禁用主机名验证
    public WebClient buildForbiddenHostNameVerify() {
        DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(null);
        clientBuilder.setSSLHostnameVerifier(hostnameVerifier);
        return this;
    }

    public WebResponse execute(WebRequest req) throws IOException {
        HttpRequestBase request = null;
        request = initMethod(req);
        buildHeaders(req, request);

        WebResponse response = new WebResponse();
        if (handler != null) {
            Object result = client.execute(request, handler);
            response.setHandlerObj(result);
            return response;
        }

        HttpResponse resp = null;
        if (context == null) {
            resp = client.execute(request);
        } else {
            resp = client.execute(request, context);
        }

        HttpEntity entity = resp.getEntity();
        if (needCacheStream) {
            entity = new BufferedHttpEntity(entity);
        }

        response.setRespEntity(entity);
        response.setStatusLine(resp.getStatusLine());
        return response;
    }

    private HttpRequestBase initMethod(WebRequest req) {
        if (req.getMethod() == null) {
            req.setMethod(RequestMethod.GET);
        }
        HttpRequestBase request = null;
        switch (req.getMethod().getVal()) {
            case "get":
                request = new HttpGet(req.getUrl());
                break;
            case "post":
                request = new HttpPost(req.getUrl());
                try {
                    ((HttpPost) request).setEntity(new UrlEncodedFormEntity(req.getRequestBody()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case "post_string":
                request = new HttpPost(req.getUrl());
                try {
                    ((HttpPost) request).setEntity(new StringEntity(req.getRequestBodyString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case "post_byte":
                request = new HttpPost(req.getUrl());
//                ((HttpPost) request).setEntity(new ByteArrayEntity());
                break;
            case "post_file":
                request = new HttpPost(req.getUrl());
                ((HttpPost) request).setEntity(new FileEntity(new File(req.getFileName())));
                break;
            default:
                request = new HttpGet(req.getUrl());
        }
        return request;
    }

    private void buildHeaders(WebRequest request, HttpRequestBase req) {
        Map<String, String> headers = request.getRequestHeaders();

        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                req.addHeader(entry.getKey(), entry.getValue());
            }
        }

        req.addHeader("Cookie", StringUtils.isNotEmpty(request.getCookie()) ? request.getCookie() : "");
    }

    private List<Header> buildDefaultHeaders() {
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("User-Agent", User_Agent));
        headers.add(new BasicHeader("Accept", Accept));
        headers.add(new BasicHeader("Accept-Language", Accept_Language));
        headers.add(new BasicHeader("Accept-Encoding", Accept_Encoding));
        headers.add(new BasicHeader("Connection", Connection));
        headers.add(new BasicHeader("Pragma", Pragma));
        headers.add(new BasicHeader("Cache-Control", Cache_Control));

        return headers;
    }


    public HttpClientContext getContext() {
        return context;
    }

    public String getCookie() {
        StringBuffer stringBuffer = new StringBuffer();

        for (Cookie cookie : context.getCookieStore().getCookies()) {
            stringBuffer.append(cookie.getName());
            stringBuffer.append("=");
            stringBuffer.append(cookie.getValue());
            stringBuffer.append(";");
        }
        return stringBuffer.toString();
    }

    public void close() {
        try {
            client.getConnectionManager().shutdown();
            System.out.println(this.connectionMonitor);
            this.connectionMonitor.close();
            ((CloseableHttpClient) client).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum ProxyType {
        PROXY_NONE("none"), PROXY_JDK("jdk"), PROXY_ADDRESS("address");

        String val;

        ProxyType(String val) {
            this.val = val;
        }

        public String getVal() {
            return val;
        }
    }


}
