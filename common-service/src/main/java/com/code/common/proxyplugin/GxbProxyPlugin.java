package com.code.common.proxyplugin;

import com.code.common.bean.CheckCookieBean;
import com.code.common.bean.LoginParam;
import com.code.common.bean.ProxyObj;
import com.code.common.crawl.WebRequest;
import com.code.common.crawl.WebResponse;
import com.code.common.crawl.WebUtils;
import com.code.common.proxy.TrialProxyPlugin;
import com.code.common.utils.JsonPathUtils;
import com.code.common.utils.PatternUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GxbProxyPlugin extends TrialProxyPlugin {
    private static String _189ProxyUrl = "http://172.19.19.16:8080/wiseproxy/service/getProxy?site=189.cn&partition=adsl&mode=";
    private static String qqProxyUrl = "http://172.19.19.16:8080/wiseproxy/service/getProxy?site=qq.com&partition=adsl&mode=";
    private static String proxyUrl = "";
    private List<LoginParam> loginParamList = null;
    private Logger logger = LoggerFactory.getLogger(GxbProxyPlugin.class);

    @Override
    public String getProxyPluginName() {
        return "GxbProxyPlugin";
    }

    @Override
    public List<LoginParam> loginParamList() {
        return loginParamList;
    }

    public GxbProxyPlugin() {
        loginParamList = new ArrayList<>();
    }

    @Override
    public ProxyObj process() {
        WebResponse response = null;
        ProxyObj proxyStr = null;
        try {
            WebRequest request = new WebRequest(qqProxyUrl);
            response = WebUtils.defaultClient().execute(request);
        } catch (IOException e) {
            logger.error("webclient error:{}", e);
        }
        String page = response.getRespText();
        if (StringUtils.isNotEmpty(page) && PatternUtils.match(page, "proxy")) {
            page = page.trim();
            logger.info("{} acquire proxyplugin:{}", getProxyPluginName(), page);
            //{"proxyplugin":"124.152.185.57:4329","provCode":"620000","createTime":1557676394000,"cityCode":"621000","timestamp":1557677535015}
            String proxy = JsonPathUtils.getValue(page, "$.proxy");
            proxyStr = new ProxyObj(proxy.split(":")[0], Integer.parseInt(proxy.split(":")[1]));
        } else {
            logger.error("{} get proxyplugin error:{}", getProxyPluginName(), page);
        }
        return proxyStr;
    }

    @Override
    public CheckCookieBean checkCookieBean() {
        return null;
    }

    @Override
    public void login(LoginParam param) {
        return;
    }
}
