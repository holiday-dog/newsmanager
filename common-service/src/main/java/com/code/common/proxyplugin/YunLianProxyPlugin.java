package com.code.common.proxyplugin;

import com.code.common.annos.ProxyOrder;
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
@ProxyOrder(order = 80)
public class YunLianProxyPlugin extends TrialProxyPlugin {
    private static String indexUrl = "http://http.yunlianip.com/Users-login.html";
    private static String getProxyUrl = "http://47.96.139.87:8081/Index-generate_api_url.html?packid=7&fa=5&qty=1&port=1&format=txt&ss=1&css=&pro=&city=";
    private static List<LoginParam> loginParamList = new ArrayList<>();
    private static Logger logger = LoggerFactory.getLogger(YunLianProxyPlugin.class);
    private static WebClient client = WebClient.buildDefaultClient().build();
    private static ConcurrentHashMap<String, String> cookies = new ConcurrentHashMap<>();

    @Override
    public String getProxyPluginName() {
        return "YunLianProxyPlugin";
    }

    @Override
    public List<LoginParam> loginParamList() {
        return loginParamList;
    }

    static {
        LoginParam param1 = new LoginParam("holiday555", "m13354612723", "13354612723");
        loginParamList.add(param1);
        LoginParam param2 = new LoginParam("happyone", "happy123", "18342238909");
        loginParamList.add(param2);
        LoginParam param3 = new LoginParam("wangheng", "heng123", "17783130253");
        loginParamList.add(param3);
        LoginParam param4 = new LoginParam("xiaozhu_177", "ty2571", "17704255028");
        loginParamList.add(param4);
//        LoginParam param5 = new LoginParam("xiaozhu_177", "ty2571", "17704255028");
//        loginParamList.add(param5);
    }

    public YunLianProxyPlugin() {
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
        try {
            WebRequest request = new WebRequest("http://http.yunlianip.com/Users-login.html");
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

        }
    }

    @Override
    public void getFreeIp(LoginParam param) {
        try {
            //获取每天的免费ip
            WebRequest request1 = new WebRequest("http://http.yunlianip.com/Index-getFree.html");
            request1.setMethod(RequestMethod.POST_STRING);
            client.execute(request1);
            logger.info("获取每天的每天的免费ip成功");

            request1 = new WebRequest("http://http.yunlianip.com/Index-getips.html");
            request1.setMethod(RequestMethod.POST_STRING);
            client.execute(request1);
        } catch (Exception e) {
            logger.error("{} error, msg:{}", getProxyPluginName(), e);
        }
    }

    @Override
    public String getCookie(String loginName) {
        return cookies.get(loginName);
    }
}
