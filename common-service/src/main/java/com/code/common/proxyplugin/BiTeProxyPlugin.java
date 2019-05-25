package com.code.common.proxyplugin;

import com.code.common.bean.CheckCookieBean;
import com.code.common.bean.LoginParam;
import com.code.common.bean.ProxyObj;
import com.code.common.crawl.RequestMethod;
import com.code.common.crawl.WebClient;
import com.code.common.crawl.WebRequest;
import com.code.common.crawl.WebResponse;
import com.code.common.proxy.TrialProxyPlugin;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

//每天免费10个
public class BiTeProxyPlugin extends TrialProxyPlugin {
    private static String indexUrl = "http://pc.bitdaili.com/index-getapi.html";
    private static String getProxyUrl = "http://47.105.97.33/Index-generate_api_url.html?packid=1&fa=0&qty=1&port=1&format=txt&ss=1&css=&pro=&city=";
    private static String getProxyUra = "http://47.105.97.33/Index-generate_api_url.html?packid=1&fa=0&qty=1&port=1&format=txt&ss=1&css=&pro=&city=";
    private static String checkCookieUrl = "http://pc.bitdaili.com/Users-index.html";
    private static String loginUrl = "http://pc.bitdaili.com/Users-login.html";
    private static List<LoginParam> loginParamList = new ArrayList<>();
    private WebClient client = WebClient.buildDefaultClient().build();
    private static Logger logger = LoggerFactory.getLogger(BiTeProxyPlugin.class);
    private static ConcurrentHashMap<String, String> cookies = new ConcurrentHashMap<>();

    @Override
    public String getProxyPluginName() {
        return "BiTeProxyPlugin";
    }

    @Override
    public List<LoginParam> loginParamList() {
        return loginParamList;
    }

    static {
//        LoginParam param1 = new LoginParam("holidaycat", "m13354612723", "13354612723");
//        loginParamList.add(param1);
//        LoginParam param2 = new LoginParam("caowen", "caowen123", "17783130253");
//        loginParamList.add(param2);
        LoginParam param3 = new LoginParam("xiaoqq", "hanxiao", "18342238909");
        loginParamList.add(param3);
//        LoginParam param4 = new LoginParam("xiaofeixia", "wangxiaofei000", "18958078576");
//        loginParamList.add(param4);
    }

    public BiTeProxyPlugin() {
    }

    @Override
    public ProxyObj process(LoginParam param) {
        ProxyObj obj = null;
        WebRequest request = new WebRequest(getProxyUrl);
        request.setCookie(getCookie(param.getUsername()));
        try {
            WebResponse response = client.execute(request);
            logger.info("getproxy page:{}", response.getRespText());
            if (StringUtils.isNotEmpty(response.getRespText())) {
                String[] proxys = response.getRespText().split(":");
                obj = new ProxyObj(proxys[0], Integer.parseInt(proxys[1]));
            } else {
                logger.error("{} request proxy fail, page:{}", getProxyPluginName(), response.getRespText());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public CheckCookieBean checkCookieBean() {
//        return new CheckCookieBean(checkCookieUrl, MatcherType.CONTAINS, "个人资料");
        return null;
    }

    @Override
    public void login(LoginParam param) {
        try {
            WebRequest request = new WebRequest(loginUrl);
            request.setMethod(RequestMethod.POST_STRING);
            request.setRequestBodyString("username=" + param.getUsername() + "&password=" + param.getPassword() + "&type=web");
            WebResponse response = client.execute(request);
            if (response.getRespText().contains("个人资料")) {
                cookies.put(param.getUsername(), client.getContextCookies());
                logger.error("{} login success!", getProxyPluginName());
            } else {
                logger.error("{} login fail!", getProxyPluginName());
            }
        } catch (Exception e) {
            logger.error("{} login error, msg:{}", getProxyPluginName(), e);
        }
    }

    @Override
    public boolean needFreeIp() {
        return true;
    }

    @Override
    public void getFreeIp(LoginParam param) {
        try {
            //获取每天的免费ip
            //{"code":0,"msg":"","data":[{"IpValidTimeZoneType":1,"IpCanUseQtyPerDay":10,"TodayUseIpQty":0,"surplusCanUseQty":10,"tiquQty":10,"subject":"1-5\u5206\u949f","fa":0,"ZoneValidTime":"2019-05-23 00:21:29"}]}
            WebRequest request1 = new WebRequest("http://pc.bitdaili.com/Index-getFree.html");
            request1.setMethod(RequestMethod.POST_STRING);
            request1.setRequestBodyString("");
            client.execute(request1);
            logger.info("获取每天的每天的免费ip成功");

            request1 = new WebRequest("http://pc.bitdaili.com/Index-getips.html");
            client.execute(request1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getCookie(String loginName) {
        if (cookies == null) {
            return StringUtils.EMPTY;
        }
        return StringUtils.isNotEmpty(cookies.get(loginName)) ? cookies.get(loginName) : genCookie(loginName);
    }
}
