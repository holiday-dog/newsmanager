package com.code.common.proxy;

import com.code.common.bean.CheckCookieBean;
import com.code.common.bean.LoginParam;
import com.code.common.crawl.WebClient;
import com.code.common.crawl.WebRequest;
import com.code.common.crawl.WebResponse;
import com.code.common.crawl.WebUtils;
import com.code.common.utils.PatternUtils;
import com.code.common.utils.RandomUtils;
import com.code.common.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.RedisConnectionFailureException;

import java.io.IOException;
import java.util.List;

//试用代理插件
public abstract class TrialProxyPlugin {
    private Logger logger = LoggerFactory.getLogger(TrialProxyPlugin.class);
    private final static String checkProxyUrl = "https://world.taobao.com/";
    private final static Integer loginRetryCount = 2;

    public abstract ProxyStr process();

    public abstract CheckCookieBean checkCookieBean();

    public abstract void login(LoginParam param);

    public abstract String getProxyPluginName();

    public abstract List<LoginParam> loginParamList();

    public Integer getLoginRetryCount() {
        return loginRetryCount;
    }

    public ProxyStr getProxy() {
        ProxyStr str = null;
        if (checkCookie()) {
            logger.info("cookie invalidate, need login");
            login(getRandomLoginParam());
        }
        for (int i = 0; i < 3; i++) {
            str = process();
            if (str != null && validateProxy(str)) {
                logger.info("retry {} times, proxy success, proxy:{}, pluginName:{}", i, str, getProxyPluginName());
                break;
            }
        }
        if (str == null) {
            logger.error("Obtain proxy fail, pluginName:{}", getProxyPluginName());
        }
        return str;
    }

    public boolean validateProxy(ProxyStr str) {
        WebClient client = WebClient.createCustome();
        client.buildProxy(WebClient.ProxyType.PROXY_ADDRESS, str.getProxyHost(), str.getProxyPort());

        WebResponse resp = null;
        try {
            resp = client.execute(new WebRequest(checkProxyUrl));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (resp.getRespText().contains("登录")) {
            return true;
        }
        return false;
    }

    public boolean checkCookie() {
        CheckCookieBean checkCookieBean = checkCookieBean();
        WebResponse response = null;
        try {
            String cookie = genCookie();
            if (StringUtils.isEmpty(cookie)) {
                cookie = checkCookieBean.getCookie();
            }
            WebRequest request = new WebRequest(checkCookieBean.getCheckCookieUrl());
            request.setCookie(cookie);
            response = WebUtils.defaultClient().execute(request);
        } catch (IOException e) {
            logger.error("webclient error:{}", e);
        }
        if (checkCookieBean.getMatcheType() == null) {
            return true;
        }
        if (StringUtils.isEmpty(response.getRespText())) {
            return false;
        }
        switch (checkCookieBean.getMatcheType().getVal()) {
            case "contains":
                return response.getRespText().contains(checkCookieBean.getMatcherStr());
            case "not_contains":
                return !response.getRespText().contains(checkCookieBean.getMatcherStr());
            case "matcher":
                return PatternUtils.match(response.getRespText(), checkCookieBean.getMatcherStr());
            case "not_matcher":
                return !PatternUtils.match(response.getRespText(), checkCookieBean.getMatcherStr());
        }
        return false;
    }

    private LoginParam getRandomLoginParam() {
        int randomIndex = RandomUtils.nextInt(loginParamList().size());
        return loginParamList().get(randomIndex);
    }

    public String genCookie() {
        try {
            String cookieKey = StringUtils.substringBefore(getProxyPluginName(), "ProxyPlugin") + "_" + "Cookie";
            return RedisUtils.getValueByKey(cookieKey);
        } catch (Exception e) {
            logger.error("error:", e);
            if (!(e instanceof RedisConnectionFailureException)) {
                throw e;
            }
        }
        return StringUtils.EMPTY;
    }

}
