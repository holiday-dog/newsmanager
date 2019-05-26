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
public class HengXingProxyPlugin extends TrialProxyPlugin {
    private static final String indexUrl = "http://h.zbzok.com/";
    private static String getProxyUrl = "http://120.79.197.226:8080/Index-generate_api_url.html?packid=1&fa=0&qty=1&port=1&format=txt&ss=1&css=&pro=&city=";
    //http://120.79.197.226:8080/Index-generate_api_url.html?packid=1&fa=0&qty=1&port=1&format=txt&ss=1&css=&pro=&city=
    private static String loginUrl = "http://h.zbzok.com/Users-login.html";
    private static List<LoginParam> loginParamList = new ArrayList<>();
    private static ConcurrentHashMap<String, String> cookies = new ConcurrentHashMap<>();
    private WebClient client = WebClient.buildDefaultClient().build();
    private static Logger logger = LoggerFactory.getLogger(HengXingProxyPlugin.class);

    @Override
    public String getProxyPluginName() {
        return "HengXingProxyPlugin";
    }

    @Override
    public List<LoginParam> loginParamList() {
        return loginParamList;
    }

    static {
        LoginParam param1 = new LoginParam("holiday333", "m13354612723", "13354612723", "m13354612723@126.com");
        loginParamList.add(param1);
        LoginParam param2 = new LoginParam("zhangna", "zhangna123", "18342238909", "m13354612723@126.com");
        loginParamList.add(param2);
        LoginParam param3 = new LoginParam("xiaoqiao", "201287", "13354612723", "m13354612723@126.com");
        loginParamList.add(param3);
        LoginParam param4 = new LoginParam("George", "George123", "18958078575", "m13354612723@126.com");
        loginParamList.add(param4);
        LoginParam param5 = new LoginParam("wangrui", "wr792143", "18958078576", "m13354612723@126.com");
        loginParamList.add(param5);
    }

    public HengXingProxyPlugin() {
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
                String[] proxys = response.getRespText().trim().split(":");
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
    public CheckCookieBean checkCookieBean(LoginParam param) {
        if (StringUtils.isEmpty(cookies.get(param.getUsername()))) {
            return new CheckCookieBean(null, null, null);
        }
        return null;
    }

    @Override
    public void login(LoginParam param) {
        WebRequest request = new WebRequest(loginUrl);
        request.setMethod(RequestMethod.POST_STRING);
        request.setRequestBodyString("username=" + param.getUsername() + "&password=" + param.getPassword() + "&type=web&pre_url=");
        try {
            WebResponse response = client.execute(request);
            if (response.getRespText().contains("\"code\":0")) {
                logger.info("{} login success", getProxyPluginName());
                String cookie = client.getContextCookies();
                cookies.put(param.getUsername(), cookie);
            } else {
                logger.error("{} login fail!", getProxyPluginName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getFreeIp(LoginParam param) {
        try {
            //获取每天的免费ip
            //{"code":0,"msg":"","data":[{"IpValidTimeZoneType":1,"IpCanUseQtyPerDay":10,"TodayUseIpQty":0,"surplusCanUseQty":10,"tiquQty":10,"subject":"1-5\u5206\u949f","fa":0,"ZoneValidTime":"2019-05-23 00:21:29"}]}
            WebRequest request1 = new WebRequest("http://h.zbzok.com/Index-getFree.html");
            request1.setMethod(RequestMethod.POST_STRING);
            client.execute(request1);
            logger.info("获取每天的每天的免费ip成功");

            request1 = new WebRequest("http://h.zbzok.com/Index-getips.html");
            request1.setMethod(RequestMethod.POST_STRING);
            client.execute(request1);
        } catch (Exception e) {
        }
    }

    @Override
    public String getCookie(String loginName) {
        return cookies.get(loginName);
    }
}
