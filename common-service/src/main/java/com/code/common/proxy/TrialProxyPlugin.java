package com.code.common.proxy;

import com.code.common.bean.CheckCookieBean;
import com.code.common.bean.LoginParam;
import com.code.common.bean.ProxyObj;
import com.code.common.crawl.WebClient;
import com.code.common.crawl.WebRequest;
import com.code.common.crawl.WebResponse;
import com.code.common.utils.PatternUtils;
import com.code.common.utils.RandomUtils;
import com.code.common.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;

//试用代理插件
public abstract class TrialProxyPlugin {
    private Logger logger = LoggerFactory.getLogger(TrialProxyPlugin.class);
    private final static String checkProxyUrl = "https://www.taobao.com/";
    private final static Integer proxyRetryCount = 3;
    private final static Integer loginRetryCount = 2;

    public abstract ProxyObj process(LoginParam param);

    public abstract CheckCookieBean checkCookieBean();

    public abstract void login(LoginParam param);

    public abstract String getProxyPluginName();

    public abstract List<LoginParam> loginParamList();

    public String getCookie(String loginName) {
        return genCookie(loginName);
    }

    public Integer getProxyRetryCount() {
        return proxyRetryCount;
    }

    public static Integer getLoginRetryCount() {
        return loginRetryCount;
    }

    public ProxyObj getProxy() {
        ProxyObj str = null;
        LoginParam param = getRandomLoginParam();
        if (!checkCookie(param)) {
            logger.info("{} cookie invalidate, need login", getProxyPluginName());
            login(param);
        }
        for (int i = 0; i < getProxyRetryCount(); i++) {
            try {
                str = process(param);
                if (str != null && validateProxy(str)) {
                    logger.info("retry {} times, proxyplugin validate success, proxyplugin:{}, pluginName:{}", i, str, getProxyPluginName());
                    break;
                }
            } catch (Exception e) {
                logger.error("acquire proxy error, msg:{}", e);
                continue;
            }
        }
        if (str == null) {
            logger.error("Obtain proxyplugin fail, pluginName:{}", getProxyPluginName());
        }
        return str;
    }

    public boolean validateProxy(ProxyObj str) {
        WebClient client = WebClient.buildCustomeClient();
        client.buildProxy(WebClient.ProxyType.PROXY_ADDRESS, str.getProxyHost(), str.getProxyPort());
        client.build();

        WebResponse resp = null;
        try {
            resp = client.execute(new WebRequest(checkProxyUrl));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean checkCookie(LoginParam param) {
        CheckCookieBean checkCookieBean = checkCookieBean();
        if (checkCookieBean == null) {
            return true;
        }
        WebResponse response = null;
        try {
            String cookie = getCookie(param.getUsername());
            if (StringUtils.isEmpty(cookie)) {
                cookie = checkCookieBean.getCookie();
            }
            WebRequest request = new WebRequest(checkCookieBean.getCheckCookieUrl());
            request.setCookie(cookie);
            response = WebClient.buildDefaultClient().build().execute(request);
            logger.info("cookie check page:{}", response.getRespText());
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
        if (CollectionUtils.isEmpty(loginParamList())) {
            return null;
        }
        int randomIndex = RandomUtils.nextInt(loginParamList().size());
        return loginParamList().get(randomIndex);
    }

    public String genCookie(String loginName) {
        try {
            if (StringUtils.isEmpty(loginName)) {
                return null;
            }
            String cookieKey = getProxyPluginName() + "_" + "Cookie" + "_" + loginName;
            return RedisUtils.getValueByKey(cookieKey);
        } catch (Exception e) {
            logger.error("error:", e);
            if (!(e instanceof RedisConnectionFailureException)) {
                throw e;
            }
        }
        return StringUtils.EMPTY;
    }

    public String genCookie(LoginParam param) {
        try {
            if (param == null || StringUtils.isEmpty(param.getUsername())) {
                return null;
            }
            String cookieKey = getProxyPluginName() + "_" + "Cookie" + "_" + param.getUsername();
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
