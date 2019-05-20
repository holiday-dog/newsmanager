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
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ProxySelector;
import java.util.*;

public class WebClient {
    private String User_Agent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:63.0) Gecko/20100101 Firefox/63.0";
    private String Referer = null;
    //    private final static String User_Agent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/536.3 (KHTML, like Gecko) Chrome/19.0.1061.1 Safari/535.3";
    private final static String Accept = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
    private final static String Accept_Language = "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2";
    private final static String Accept_Encoding = "gzip, deflate, br";
    private final static String Connection = "keep-alive";
    private final static String Pragma = "no-cache";
    private final static String Cache_Control = "no-cache";
    private static Logger logger = LoggerFactory.getLogger(WebClient.class);

    //线程安全
    private static HttpClient client = null;

    //自定义返回结果处理
    private ResponseHandler handler;

    private HttpClientContext context;

    private Boolean needContext;

    private HttpClientBuilder clientBuilder;

    private IdleConnectionMonitor connectionMonitor;

    private RequestConfig config;

    private HttpHost proxy;

    private Boolean needCacheStream = false;

    public String getUser_Agent() {
        return User_Agent;
    }

    public void setUser_Agent(String user_Agent) {
        User_Agent = user_Agent;
    }

    public String getReferer() {
        return Referer;
    }

    public void setReferer(String referer) {
        Referer = referer;
    }

    private WebClient() {
    }

    public static WebClient buildDefaultClient() {
        WebClient webClient = new WebClient();
        webClient.client = HttpClients.createDefault();
        return webClient;
    }

    public static WebClient buildCustomeClient() {
        WebClient webClient = new WebClient();
        return webClient.init();
    }

    public WebClient init() {
        this.setUser_Agent(RandomUAUtils.getRandomUA(BrowersUA.FIREFOX));
        clientBuilder = HttpClients.custom();
        return this;
    }

    //自定义实现httpclient
    public void buildCustomeClient(HttpClient client) {
        this.client = client;
    }

    //不受自定义client的影响，可以直接跟在build的后面
    public WebClient buildContext() {
        context = HttpClientContext.create();
        return this;
    }

    public WebClient buildUaAndReferer(BrowersUA browersUA, String referer) {
        this.setUser_Agent(RandomUAUtils.getRandomUA(browersUA));
        this.setReferer(referer);
        return this;
    }

    public WebClient build() {
        clientBuilder.setDefaultHeaders(buildDefaultHeaders());
        client = clientBuilder.build();
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

        response = buildWebResponse(resp);
        response.setRespEntity(entity);
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

//        if (headers != null && headers.size() > 0) {
//            for (Map.Entry<String, String> entry : headers.entrySet()) {
//                req.addHeader(entry.getKey(), entry.getValue());
//            }
//        }
        List<Header> defaultHeaders = buildDefaultHeaders();
        for (Header header : defaultHeaders) {
            req.addHeader(header.getName(), header.getValue());
        }
        if (headers != null && headers.size() > 0) {
            if (StringUtils.isNotEmpty(headers.get("User-Agent")) && !"Apache-HttpClient".equals(headers.get("User-Agent"))) {
                req.addHeader("User-Agent", headers.get("User-Agent"));
            }
            if (StringUtils.isNotEmpty(headers.get("Referer"))) {
                req.addHeader("Referer", headers.get("Referer"));
            }
            if (StringUtils.isNotEmpty(request.getCookie())) {
                req.addHeader("Cookie", request.getCookie());
            }
        }
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

    private WebResponse buildWebResponse(HttpResponse resp) {
        WebResponse response = new WebResponse();

        response.setStatusLine(resp.getStatusLine());
        response.setHeaders(resp.getAllHeaders());
        return response;
    }


    public HttpClientContext getContext() {
        return context;
    }

    public String getContextCookies() {
        StringBuffer stringBuffer = new StringBuffer();

        for (Cookie cookie : context.getCookieStore().getCookies()) {
            stringBuffer.append(cookie.getName());
            stringBuffer.append("=");
            stringBuffer.append(cookie.getValue());
            stringBuffer.append(";");
        }
        return stringBuffer.toString();
    }

    public static String getClientCookies(WebRequest request, WebResponse response) {
        Set<Cookie> cookies = new HashSet<>();
        if (StringUtils.isNotEmpty(request.getCookie())) {
            cookies.addAll(parse(request.getCookie()));
        }
        if (StringUtils.isNotEmpty(response.getCookie())) {
            cookies.addAll(parse(response.getCookie()));
        }

        StringBuffer cookieStr = new StringBuffer();
        for (Cookie cookie : cookies) {
            cookieStr.append(cookie.getName());
            cookieStr.append("=");
            cookieStr.append(cookie.getValue());
            cookieStr.append(";");
        }

        return cookieStr.toString();
    }

    private static Set<Cookie> parse(String cookieStr) {
        if (StringUtils.isEmpty(cookieStr)) {
            return new HashSet<>();
        }
        String[] cookieStrs = cookieStr.split(";");

        Set<Cookie> setCookies = new HashSet<>();
        for (String cs : cookieStrs) {
            try {
                String csName = StringUtils.substringBefore(cs, "=");
                String csVal = StringUtils.substringAfter(cs, "=");
                if (StringUtils.isNotEmpty(csName) && !"expires".equals(csName.trim()) && !"Max-Age".equals(csName.trim()) && !"path".equals(csName.trim())) {
                    Cookie cookie = new BasicClientCookie(csName, csVal);
                    setCookies.add(cookie);
                }
            } catch (Exception e) {
                logger.error("parser cookieStr error:{}", cs);
            }
        }
        return setCookies;
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
