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

//每天免费10
public class HeiDongProxyPlugin extends TrialProxyPlugin {
    private static String indexUrl = "http://http.hunbovps.com/getapi.html";
    private static String getProxyUrl = "http://t.11jsq.com/index.php/api/entry?method=proxyServer.generate_api_url&packid=1&fa=0&fetch_key=&qty=1&time=1&pro=&city=&port=1&format=txt&ss=1&css=&dt=1&specialTxt=3&specialJson=";
    private static List<LoginParam> loginParamList = new ArrayList<>();
    private static Logger logger = LoggerFactory.getLogger(HeiDongProxyPlugin.class);
    private WebClient client = WebClient.buildDefaultClient().build();

    @Override
    public String getProxyPluginName() {
        return "HeiDongProxyPlugin";
    }

    @Override
    public List<LoginParam> loginParamList() {
        return loginParamList;
    }

    static {
        LoginParam param1 = new LoginParam("holiday111", "m13354612723", "13354612723");
        loginParamList.add(param1);
        LoginParam param2 = new LoginParam("liuxing", "9823", "17783130253");
        loginParamList.add(param2);
        LoginParam param3 = new LoginParam("zhiqiang", "zhao123", "18342238909");
        loginParamList.add(param3);
        LoginParam param4 = new LoginParam("liuxing", "9823", "17783130253");
        loginParamList.add(param4);
    }

    public HeiDongProxyPlugin() {
    }

    @Override
    public ProxyObj process(LoginParam param) {
        ProxyObj obj = null;
        WebRequest request = new WebRequest(getProxyUrl);
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
    public CheckCookieBean checkCookieBean() {
        return null;
    }

    @Override
    public void login(LoginParam param) {

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
            WebRequest request1 = new WebRequest("http://http.hunbovps.com/Index-getFree.html");
            request1.setMethod(RequestMethod.POST_STRING);
            client.execute(request1);
            logger.info("获取每天的每天的免费ip成功");

            request1 = new WebRequest("http://http.hunbovps.com/Index-getips.html");
            request1.setMethod(RequestMethod.POST_STRING);
            client.execute(request1);
        } catch (Exception e) {
            logger.error("{} error, msg:{}", getProxyPluginName(), e);
        }
    }
}
