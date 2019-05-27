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
import com.code.common.utils.PatternUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

//每天免费10个
@ProxyOrder(order = 90)
public class JingLingTrialProxyPlugin extends TrialProxyPlugin {
    private static String indexUrl = "http://www.jinglingdaili.com/Users-login.html";
    private static String getProxyUrl = "http://t.11jsq.com/index.php/api/entry?method=proxyServer.generate_api_url&packid=1&fa=0&fetch_key=&qty=1&time=1&pro=&city=&port=1&format=txt&ss=1&css=&dt=1&specialTxt=3&specialJson=";
    //    private static String getProxyUra = "http://t.11jsq.com/index.php/api/entry?method=proxyServer.generate_api_url&packid=1&fa=0&fetch_key=&qty=1&time=1&pro=&city=&port=1&format=txt&ss=1&css=&dt=1&specialTxt=3&specialJson=";
    private static List<LoginParam> loginParamList = new ArrayList<>();
    private static Logger logger = LoggerFactory.getLogger(JingLingTrialProxyPlugin.class);
    private static ConcurrentHashMap<String, String> cookies = new ConcurrentHashMap();
    private WebClient client = WebClient.buildDefaultClient().build();

    @Override
    public String getProxyPluginName() {
        return "JingLingTrialProxyPlugin";
    }

    @Override
    public List<LoginParam> loginParamList() {
        return loginParamList;
    }

    static {
//        LoginParam param1 = new LoginParam("holidaydog", "m13354612723", "13354612723");
//        loginParamList.add(param1);
        LoginParam param2 = new LoginParam("dto312", "lixian123", "18958078576");
        loginParamList.add(param2);
    }

    public JingLingTrialProxyPlugin() {
    }

    @Override
    public ProxyObj process(LoginParam param) {
        ProxyObj obj = null;
        WebRequest request = new WebRequest(getProxyUrl);
        request.setCookie(getCookie(param.getUsername()));
        try {
            WebResponse response = client.execute(request);
            logger.info("getproxy page:{}", response.getRespText());
            if (response.getRespText().contains("\"code\":5")) {
                //添加ip白名单
                request = new WebRequest("http://www.jinglingdaili.com/Users-whiteIpAdd.html");
                request.setMethod(RequestMethod.POST_STRING);
                request.setRequestBodyString(PatternUtils.groupOne(response.getRespText(), "(\\d+\\.\\d+\\.\\d+\\.\\d+)", 1));
                response = client.execute(request);

                getFreeIp(param);

                request = new WebRequest(getProxyUrl);
                response = client.execute(request);
            }
            if (StringUtils.isNotEmpty(response.getRespText()) && PatternUtils.match(response.getRespText(), "\"code\":0")) {
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
            WebRequest request = new WebRequest("http://www.jinglingdaili.com/Users-login.html");
            request.setMethod(RequestMethod.POST_STRING);
            request.setRequestBodyString("username=" + param.getUsername() + "&password=" + param.getPassword());
            WebResponse response = client.execute(request);
            cookies.put(param.getUsername(), client.getContextCookies());
//            logger.info("login page:{}", response.getRespText());
        } catch (Exception e) {
            logger.error("{} error, msg:{}", getProxyPluginName(), e);
        }
    }

    @Override
    public void getFreeIp(LoginParam param) {
        try {
            WebRequest request = new WebRequest("http://www.jinglingdaili.com/Index-getapi.html");
            client.execute(request);

            request = new WebRequest("http://www.jinglingdaili.com/Index-getips.html");
            request.setMethod(RequestMethod.POST_STRING);
            WebResponse response = client.execute(request);
            logger.info("login page:{}", response.getRespText());
        } catch (Exception e) {
            logger.error("{} error, msg:{}", getProxyPluginName(), e);
        }
    }

    @Override
    public String getCookie(String loginName) {
        return cookies.get(loginName);
    }
}
