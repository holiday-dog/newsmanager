package com.code.common.proxyplugin;

import com.code.common.bean.CheckCookieBean;
import com.code.common.bean.LoginParam;
import com.code.common.bean.ProxyObj;
import com.code.common.crawl.WebClient;
import com.code.common.crawl.WebRequest;
import com.code.common.crawl.WebResponse;
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
import java.util.concurrent.ConcurrentHashMap;

//每天免费20个
public class ZhiMaProxyPlugin extends TrialProxyPlugin {
    private static String indexUrl = "http://h.zhimaruanjian.com/";
    private static String loginUrl = "http://wapi.http.cnapi.cc/index/users/login_do?jsonpcallback=jQuery1124038366609614490454_%s&phone=%s&password=%s&remember=false&_=%s";
    private static String getProxyUrl = "http://webapi.http.zhimacangku.com/getip?num=1&type=1&pro=&city=0&yys=0&port=1&pack=%s&ts=0&ys=0&cs=0&lb=1&sb=0&pb=4&mr=1&regions=";
    private static String checkCookieUrl = "http://wapi.http.cnapi.cc/index/users/user_info?jsonpcallback=jQuery11240725991062937152_%s&_=%s";
    private static List<LoginParam> loginParamList = new ArrayList<>();
    private Logger logger = LoggerFactory.getLogger(ZhiMaProxyPlugin.class);
    private WebClient client = WebClient.buildDefaultClient().build();
    private static ConcurrentHashMap<String, String> cookies = new ConcurrentHashMap();
    //是否需要点击套餐
    private boolean flag = true;

    @Override
    public String getProxyPluginName() {
        return "ZhiMaProxyPlugin";
    }

    static {
        LoginParam param1 = new LoginParam("holiday123", "m13354612723", "13354612723");
        param1.setProxyUserId("52239");
        loginParamList.add(param1);
        LoginParam param2 = new LoginParam("unlessone", "m18342238909", "18342238909");
        param2.setProxyUserId("52417");
        loginParamList.add(param2);
        LoginParam param3 = new LoginParam("dog123", "xyjabc", "17783130253");
        param3.setProxyUserId("53471");
        loginParamList.add(param3);
        LoginParam param4 = new LoginParam("zhangwei2099", "zw452103", "15754335612");
        param4.setProxyUserId("53474");
        loginParamList.add(param4);
        LoginParam param5 = new LoginParam("xiaowang12345", "wangxiao7793", "18958078576");
        param5.setProxyUserId("53470");
        loginParamList.add(param5);
    }

    public ZhiMaProxyPlugin() {
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
            WebRequest request = new WebRequest(String.format(getProxyUrl, param.getProxyUserId()));
            response = client.execute(request);
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
    public boolean needFreeIp() {
        return true;
    }

    @Override
    public void getFreeIp(LoginParam param) {
        try {
            Long ts = new Date().getTime();
            String freeIpUrl = "http://wapi.http.cnapi.cc/index/users/get_day_free_pack?jsonpcallback=jQuery112408064141149957438_" + ts + "&_=" + ts;
            WebRequest freeReq = new WebRequest(freeIpUrl);
            client.execute(freeReq);

            ts = new Date().getTime();
            String getFreeUrl = "http://wapi.http.cnapi.cc/index/api/new_get_ips?jsonpcallback=jQuery112401587584771647499_" + ts + "&num=1&package_id=" + param.getProxyUserId() +
                    "&type=1&pro_id=&port_type=1&city_id=&yys=0&time_show=false&city_show=false&yys_show=false&manyregions=&region_type=1&line_break=1&special_break=&port_bit=4&m_repeat=1&pack_type=pack&long_city=&_=" + ts;
            WebRequest getFreeReq = new WebRequest(getFreeUrl);
            client.execute(getFreeReq);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public CheckCookieBean checkCookieBean() {
        Long timestamp = new Date().getTime();
        String finalCheckCookieUrl = String.format(checkCookieUrl, timestamp, timestamp);
        return new CheckCookieBean(finalCheckCookieUrl, MatcherType.CONTAINS, "\\\\\\\"ret\\\\\\\":0");
    }

    @Override
    public void login(LoginParam param) {
        if (param == null) {
            logger.error("loginParams is null");
            return;
        }
        Long timestamp = new Date().getTime();
        String finialLoginUrl = String.format(loginUrl, timestamp, param.getUsername(), param.getPassword(), timestamp);
        WebResponse response = null;
        try {
            WebRequest request = new WebRequest(finialLoginUrl);
            response = client.execute(request);
        } catch (IOException e) {
            logger.error("{} webclient error:{}", getProxyPluginName(), e);
        }
        logger.info("login page:{}", response.getRespText());
        if (PatternUtils.match(response.getRespText(), "\\\\\\\"ret\\\\\\\":0")) {
            logger.info("{} login success", getProxyPluginName());
            cookies.put(param.getUsername(), client.getContextCookies());
            return;
        } else {
            logger.error("{} login fail", getProxyPluginName());
        }
    }

    @Override
    public String getCookie(String loginName) {
        if (!cookies.contains(loginName)) {
            return StringUtils.EMPTY;
        }
        return StringUtils.isEmpty(cookies.get(loginName)) ? genCookie(loginName) : cookies.get(loginName);
    }
}
