package com.code.common.proxy;

import com.code.common.bean.CheckCookieBean;
import com.code.common.bean.LoginParam;
import com.code.common.crawl.WebClient;
import com.code.common.crawl.WebRequest;
import com.code.common.crawl.WebResponse;
import com.code.common.crawl.WebUtils;
import com.code.common.enums.MatcherType;
import com.code.common.utils.PatternUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//极光代理（每天50个）
public class JiGuangProxyPlugin extends TrialProxyPlugin {
    private static String indexUrl = "http://h.jiguangdaili.com/";
    private static String loginUrl = "http://webapi.jghttp.golangapi.com/index/users/login_do?jsonpcallback=jQuery112408231205000709008_%s&phone=%s&password=%s&remember=true&_=%s";
    private static String checkCookieUrl = "http://h.jiguangdaili.com/ucenter/?first_time=1&jsonpcallback=jQuery112405316686798599242_%s&_=%s";
    private static String getProxyUrl = "http://d.jghttp.golangapi.com/getip?num=1&type=1&pro=&city=0&yys=0&port=1&pack=%s&ts=0&ys=0&cs=0&lb=1&sb=0&pb=4&mr=1&regions=";
    private static List<LoginParam> loginParams = null;
    private WebClient client = WebUtils.defaultClient();
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
    public CheckCookieBean checkCookieBean() {
        Long timestamp = new Date().getTime();
        String finalCheckCookieUrl = String.format(checkCookieUrl, timestamp, timestamp);
        return new CheckCookieBean(finalCheckCookieUrl, MatcherType.CONTAINS, "");
    }

    public JiGuangProxyPlugin() {
        init();
    }

    //初始化登录信息
    private void init() {
        loginParams = new ArrayList<>();
        LoginParam param1 = new LoginParam("holiday123", "m13354612723", "13354612723");
        loginParams.add(param1);
    }

    @Override
    public ProxyStr process() {
        String packId = "9526";
        WebResponse response = null;
        try {
            WebRequest request = new WebRequest(String.format(getProxyUrl, packId));
//            request.setCookie(genCookie());
            request.setCookie("UM_distinctid=16a98537165221-059a3ab70d408b-4a546e-13c680-16a985371663f7; CNZZDATA1273135583=2127688764-1557331792-%7C1557331792");
            response = WebUtils.defaultClient().execute(request);
        } catch (IOException e) {
            logger.error("webclient error:{}", e);
        }
        String page = response.getRespText();
        if (StringUtils.isNotEmpty(page) && !page.contains("false")) {
            //114.100.0.10:4537
            //有换行符
            ProxyStr str = new ProxyStr(page.split(":")[0], Integer.parseInt(page.split(":")[1].trim()));
        } else {
            logger.info("get proxy error:{}", page);
        }
        return null;
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
            try {
                response = client.execute(new WebRequest(finialLoginUrl));
            } catch (IOException e) {
                logger.error("webclient error:{}", e);
            }
            if (PatternUtils.match(response.getRespText(), "\"ret\":0")) {
                logger.info("retry {} times, login success", i);
                return;
            }
        }
        logger.info("name:{}, login fails", getProxyPluginName());
    }
}
