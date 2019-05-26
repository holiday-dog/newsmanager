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
import com.code.common.utils.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GxbProxyPlugin extends TrialProxyPlugin {
    private static String[] proxyLocations = {"qq.com", "189.cn"};
    private static String proxyTemplateUrl = "http://172.19.19.16:8080/wiseproxy/service/getProxy?site=%s&partition=adsl&mode=";
    private static List<LoginParam> loginParamList = new ArrayList<>();
    private Logger logger = LoggerFactory.getLogger(GxbProxyPlugin.class);

    @Override
    public String getProxyPluginName() {
        return "GxbProxyPlugin";
    }

    @Override
    public List<LoginParam> loginParamList() {
        return loginParamList;
    }

    @Override
    public ProxyObj process(LoginParam param) {
        WebResponse response = null;
        ProxyObj proxyStr = null;
        try {
            String proxyUrl = String.format(proxyTemplateUrl, getRandomProxyLocation());
            logger.info("use proxy localtion is :{}", proxyUrl);
            WebRequest request = new WebRequest(proxyUrl);
            response = WebUtils.defaultClient().execute(request);
        } catch (IOException e) {
            logger.error("webclient error:{}", e);
        }
        String page = response.getRespText();
        if (StringUtils.isNotEmpty(page) && PatternUtils.match(page, "proxy")) {
            page = page.trim();
            logger.info("{} acquire proxyplugin:{}", getProxyPluginName(), page);
            //{"proxyplugin":"124.152.185.57:4329","provCode":"620000","createTime":1557676394000,"cityCode":"621000","timestamp":1557677535015}
            String proxy = (String) JsonPathUtils.getValue(page, "$.proxy");
            proxyStr = new ProxyObj(proxy.split(":")[0], Integer.parseInt(proxy.split(":")[1]));
        } else {
            logger.error("{} get proxyplugin error:{}", getProxyPluginName(), page);
        }
        return proxyStr;
    }

    @Override
    public CheckCookieBean checkCookieBean(LoginParam param) {
        return null;
    }

    private String getRandomProxyLocation() {
        if (proxyLocations == null) {
            return null;
        }
        int randomIndex = RandomUtils.nextInt(proxyLocations.length);
        return proxyLocations[randomIndex];
    }
}
