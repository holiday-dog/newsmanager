package com.code.common.crawl;

import com.code.common.exception.CodeException;
import com.code.common.utils.PatternUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.entity.BufferedHttpEntity;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class WebResponse {
    private HttpEntity respEntity;

    private String respText;

    private Object handlerObj;

    private StatusLine statusLine;

    private Header[] headers;

    private String cookie;

    private Charset charset;

    public void setHandlerObj(Object handlerObj) {
        this.handlerObj = handlerObj;
    }

    public Object getHandlerObj() {
        return handlerObj;
    }

    public void setRespEntity(HttpEntity respEntity) {
        this.respEntity = respEntity;
    }

    public void setStatusLine(StatusLine statusLine) {
        this.statusLine = statusLine;
    }

    public InputStream getStream() {
        if (!(respEntity instanceof BufferedHttpEntity)) {
            throw new CodeException("返回内容未被缓存，输出流为空");
        }
        try {
            return respEntity.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getRespText(Charset charset) {
        if (StringUtils.isNotEmpty(respText)) {
            return respText;
        }
        try {
            respText = IOUtils.toString(respEntity.getContent(), charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return respText;
    }

    public String getRespText() {
        if (charset != null) {
            return getRespText(charset);
        }
        Charset charset = Charset.defaultCharset();
        if (headers != null && headers.length > 1) {
            for (Header header : headers) {
                if ("Content-Type".equals(header.getName())) {
                    charset = getContentCharset(header.getValue());
                    this.charset = charset;
                }
            }
        }
        return getRespText(charset);
    }

    public Header[] getHeaders() {
        return headers;
    }

    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }

    public String getCookie() {
        if (StringUtils.isNotEmpty(cookie)) {
            return cookie;
        }
        if (headers == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (Header header : headers) {
            if ("Set-Cookie".equals(header.getName()) || "Set-Cookie2".equals(header.getName())) {
                stringBuffer.append(header.getValue() + ";");
            }
        }
        cookie = stringBuffer.deleteCharAt(stringBuffer.length() - 1).toString();
        return cookie;
    }

    public Charset getCharset() {
        return charset;
    }

    public Integer statusCode() {
        return statusLine.getStatusCode();
    }

    public Charset getContentCharset(String value) {
        if (PatternUtils.matchNoCase(value, "(charset)\\s?=\\s?(utf-?8)")) {
            return Charset.forName("utf8");
        } else if (PatternUtils.matchNoCase(value, "(charset)\\s?=\\s?(gbk)")) {
            return Charset.forName("gbk");
        } else if (PatternUtils.matchNoCase(value, "(charset)\\s?=\\s?(gb2312)")) {
            return Charset.forName("gb2312");
        } else if (PatternUtils.matchNoCase(value, "(charset)\\s?=\\s?(iso-?8859-?1)")) {
            return Charset.forName("iso8859-1");
        }

        return Charset.defaultCharset();
    }
}
