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
    private final static Integer proxyRetryCount = 1;
    private final static Integer loginRetryCount = 2;

    public abstract String getProxyPluginName();

    public abstract List<LoginParam> loginParamList();

    public abstract ProxyObj process(LoginParam param);

    public boolean needFreeIp() {
        return false;
    }

    public void getFreeIp(LoginParam param) {
        return;
    }

    public CheckCookieBean checkCookieBean(LoginParam param) {
        return null;
    }

    public void login(LoginParam param) {
        return;
    }

    //默认从redis中获取cookie
    public String getCookie(String loginName) {
        return genCookie(loginName);
    }

    //默认不需要验证代理
    public boolean needValidateProxy() {
        return false;
    }

    public Integer getProxyRetryCount() {
        return proxyRetryCount;
    }

    public static Integer getLoginRetryCount() {
        return loginRetryCount;
    }

    public ProxyObj getProxy() {
        boolean validate = false;
        ProxyObj obj = null;
        LoginParam param = getRandomLoginParam();
        if (!checkCookie(param)) {
            logger.info("{} cookie invalidate, need login", getProxyPluginName());
            login(param);
        }
        if (needFreeIp()) {
            getFreeIp(param);
        }
        for (int i = 0; i < getProxyRetryCount(); i++) {
            try {
                obj = process(param);
                if (obj != null && (!needValidateProxy() || validateProxy(obj))) {
                    validate = true;
                    logger.info("retry {} times, proxyplugin validate success, proxy-address:{}, pluginName:{}", i, obj, getProxyPluginName());
                    break;
                }
            } catch (Exception e) {
                logger.error("acquire proxy error, msg:{}", e);
                continue;
            }
        }
        if (obj == null || !validate) {
            logger.error("Obtain proxyplugin fail, pluginName:{}", getProxyPluginName());
            return null;
        }
        return obj;
    }

    public boolean validateProxy(ProxyObj str) {
        WebClient client = WebClient.buildCustomeClient();
        client.buildProxy(WebClient.ProxyType.PROXY_ADDRESS, str.getProxyHost(), str.getProxyPort());
        client.buildConnectAndSocketTime(5000, 3000);
        client.build();

        WebResponse resp = null;
        try {
            resp = client.execute(new WebRequest(checkProxyUrl));
            if (resp.statusCode() == 200) {
                return true;
            }
            logger.error("request taobao error:statusCode:{}", resp.statusCode());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean checkCookie(LoginParam param) {
        CheckCookieBean checkCookieBean = checkCookieBean(param);
        if (checkCookieBean == null) {
            return true;
        }
        if (checkCookieBean.getCheckCookieUrl() == null || checkCookieBean.getMatcherStr() == null || checkCookieBean.getMatcheType() == null) {
            //不需要检查cookie，直接进行登录
            return false;
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
//            logger.info("cookie check page:{}", response.getRespText());
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
