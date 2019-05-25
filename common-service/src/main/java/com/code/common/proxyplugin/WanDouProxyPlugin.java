package com.code.common.proxyplugin;

import com.code.common.bean.CheckCookieBean;
import com.code.common.bean.LoginParam;
import com.code.common.bean.ProxyObj;
import com.code.common.crawl.WebClient;
import com.code.common.crawl.WebRequest;
import com.code.common.crawl.WebResponse;
import com.code.common.proxy.TrialProxyPlugin;
import com.code.common.utils.JsonPathUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//每天免费20个
public class WanDouProxyPlugin extends TrialProxyPlugin {
    private static final String indexUrl = "https://www.wandouip.com/";
    private static String getProxyUrl = "http://api.wandoudl.com/api/ip?app_key=83eb53ac2ce277a1f336cdc582bc8c19&pack=0&num=1&xy=1&type=2&lb=&mr=2&";
    private List<LoginParam> loginParamList = null;
    private static Logger logger = LoggerFactory.getLogger(WanDouProxyPlugin.class);
    private static WebClient client = WebClient.buildDefaultClient().build();

    @Override
    public String getProxyPluginName() {
        return "WanDouProxyPlugin";
    }

    public WanDouProxyPlugin() {
        loginParamList = new ArrayList<>();
        LoginParam param1 = new LoginParam("m13354612723@163.com", "Mm13354612723", "13354612723");
        loginParamList.add(param1);
        LoginParam param2 = new LoginParam("18342238909@qq.com", "qwexhs123A", "18342238909");
        loginParamList.add(param2);
        LoginParam param3 = new LoginParam("17783130253@126.com", "DYdog2419", "17783130253");
        loginParamList.add(param3);
        LoginParam param4 = new LoginParam("18958078575@163.com", "T19900327zws", "189580785753");
        loginParamList.add(param4);
        LoginParam param5 = new LoginParam("17704255028@163.com", "DYdog2419", "13354612723");
        loginParamList.add(param5);
        LoginParam param6 = new LoginParam("17704255028@163.com", "Rt2334", "17704255028");
        loginParamList.add(param6);
    }

    @Override
    public List<LoginParam> loginParamList() {
        return loginParamList;
    }


    @Override
    public ProxyObj process(LoginParam param) {
        ProxyObj obj = null;
        WebRequest request = new WebRequest(getProxyUrl);
        try {
            WebResponse response = client.execute(request);
            logger.info("getproxy page:{}", response.getRespText());
            if (StringUtils.isNotEmpty(response.getRespText())) {
                String proxys = response.getRespText().trim();
                String proxyHost = (String) JsonPathUtils.getValue(proxys, "$.data[0].ip");
                Integer proxyPort = (Integer) JsonPathUtils.getValue(proxys, "$.data[0].port");
                obj = new ProxyObj(proxyHost, proxyPort);
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
        return null;
    }

    @Override
    public void login(LoginParam param) {

    }

}
