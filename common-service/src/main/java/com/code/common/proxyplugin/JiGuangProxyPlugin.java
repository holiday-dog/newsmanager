package com.code.common.proxyplugin;

import com.code.common.bean.CheckCookieBean;
import com.code.common.bean.LoginParam;
import com.code.common.bean.ProxyObj;
import com.code.common.crawl.WebClient;
import com.code.common.crawl.WebRequest;
import com.code.common.crawl.WebResponse;
import com.code.common.crawl.WebUtils;
import com.code.common.enums.MatcherType;
import com.code.common.proxy.TrialProxyPlugin;
import com.code.common.utils.PatternUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//极光代理（每天50个）（联系客服一天200个）
@Deprecated
public class JiGuangProxyPlugin extends TrialProxyPlugin {
    private static String indexUrl = "http://h.jiguangdaili.com/";
    private static String loginUrl = "http://webapi.jghttp.golangapi.com/index/users/login_do?jsonpcallback=jQuery112408231205000709008_%s&phone=%s&password=%s&remember=true&_=%s";
    private static String checkCookieUrl = "http://webapi.jghttp.golangapi.com/index/users/user_info?jsonpcallback=jQuery112402374532623441622_%s&_=%s";
    private static String getProxyUrl = "http://d.jghttp.golangapi.com/getip?num=1&type=1&pro=&city=0&yys=0&port=1&pack=%s&ts=0&ys=0&cs=0&lb=1&sb=0&pb=45&mr=1&regions=";
    private static List<LoginParam> loginParams = null;
    private WebClient client = WebUtils.defaultClient();
    private String cookies = "";
    private static Logger logger = LoggerFactory.getLogger(JiGuangProxyPlugin.class);

    @Override
    public String getProxyPluginName() {
        return "JiGuangProxyPlugin";
    }

    @Override
    public List<LoginParam> loginParamList() {
        return loginParams;
    }

    @Override
    public CheckCookieBean checkCookieBean(LoginParam param) {
        Long timestamp = new Date().getTime();
        String finalCheckCookieUrl = String.format(checkCookieUrl, timestamp, timestamp);
        return new CheckCookieBean(finalCheckCookieUrl, MatcherType.CONTAINS, "\\\\\\\"ret\\\\\\\":0");
    }

    public JiGuangProxyPlugin() {
        init();
    }

    //初始化登录信息
    private void init() {
        loginParams = new ArrayList<>();
        LoginParam param1 = new LoginParam("holiday123", "m13354612723", "13354612723");
        param1.setProxyUserId("9677");
//        LoginParam param2 = new LoginParam("gxb123", "m18342238909", "18342238909");
        loginParams.add(param1);
//        loginParams.add(param2);
    }

    @Override
    public ProxyObj process(LoginParam param) {
        WebResponse response = null;
        ProxyObj proxyStr = null;
        try {
            WebRequest request = new WebRequest(String.format(getProxyUrl, param.getProxyUserId()));
            String redisCookie = genCookie(param);
            request.setCookie(StringUtils.isEmpty(redisCookie) ? cookies : redisCookie);
            response = WebUtils.defaultClient().execute(request);
        } catch (IOException e) {
            logger.error("webclient error:{}", e);
        }
        String page = response.getRespText();
        if (StringUtils.isNotEmpty(page) && !page.contains("\"success\":false")) {
            page = page.trim();
            logger.info("{} acquire proxyplugin:{}", getProxyPluginName(), page);
            proxyStr = new ProxyObj(page.split(":")[0], Integer.parseInt(page.split(":")[1]));
        } else {
            logger.error("{} get proxyplugin error:{}", getProxyPluginName(), page);
        }
        return proxyStr;
    }

    @Override
    public void login(LoginParam param) {
        if (param == null) {
            logger.error("loginParams is null");
            return;
        }
        for (int i = 0; i < getLoginRetryCount(); i++) {
            Long timestamp = new Date().getTime();
            String finialLoginUrl = String.format(loginUrl, timestamp, param.getUsername(), param.getPassword(), timestamp);
            WebResponse response = null;
            WebRequest request = null;
            try {
                request = new WebRequest(finialLoginUrl);
                response = client.execute(request);
            } catch (IOException e) {
                logger.error("{} webclient error:{}", getProxyPluginName(), e);
                continue;
            }
            logger.info("login page:{}", response.getRespText());
            if (PatternUtils.match(response.getRespText(), "\\\\\\\"ret\\\\\\\":0")) {
                logger.info("retry {} times, {} login success", i, getProxyPluginName());
                cookies = WebClient.getClientCookies(request, response);
                return;
            } else {
                break;
            }
        }
        logger.info("name:{}, login fails", getProxyPluginName());
    }
}
