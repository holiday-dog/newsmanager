package com.code.common.crawl;

import com.code.common.exception.CodeException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.BufferedHttpEntity;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class WebResponse {
    private HttpEntity respEntity;

    private String respText;

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

    public String getRespText() {
        if (StringUtils.isNotEmpty(respText)) {
            return respText;
        }
        try {
            respText = IOUtils.toString(respEntity.getContent(), Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return respText;
    }
}
