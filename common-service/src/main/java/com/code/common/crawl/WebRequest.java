package com.code.common.crawl;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebRequest {
    private String url;

    private String cookie;

    private RequestMethod method;

    private Map<String, String> requestHeaders;

    private List<NameValuePair> requestBody;

    private String fileName;

    private String requestBodyString;

    private String charset;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public void setMethod(RequestMethod method) {
        this.method = method;
    }

    public List<NameValuePair> getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(List<NameValuePair> requestBody) {
        this.requestBody = requestBody;
    }

    public void addRequestBody(String name, String value) {
        if (CollectionUtils.isEmpty(requestBody)) {
            requestBody = new ArrayList<>();
        }
        NameValuePair pair = new BasicNameValuePair(name, value);
        requestBody.add(pair);
    }

    public Map<String, String> getRequestHeaders() {
        if (requestHeaders == null) {
            requestHeaders = new HashMap<>();
        }
        return requestHeaders;
    }

    public void addRequestHeaders(Map<String, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public void addRequestHeader(String headerName, String headerVal) {
        if (requestHeaders == null) {
            requestHeaders = new HashMap<>();
        }
        requestHeaders.put(headerName, headerVal);
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getRequestBodyString() {
        return requestBodyString;
    }

    public void setRequestBodyString(String requestBodyString) {
        this.requestBodyString = requestBodyString;
    }

    public WebRequest(String url) {
        this.url = url;
    }

    public WebRequest(String url, RequestMethod method, List<NameValuePair> requestBody) {
        this.url = url;
        this.method = method;
        this.requestBody = requestBody;
    }
}
