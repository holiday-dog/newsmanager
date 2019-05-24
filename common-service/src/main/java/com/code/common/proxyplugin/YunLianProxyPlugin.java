package com.code.common.proxyplugin;

import com.code.common.bean.CheckCookieBean;
import com.code.common.bean.LoginParam;
import com.code.common.bean.ProxyObj;
import com.code.common.proxy.TrialProxyPlugin;

import java.util.ArrayList;
import java.util.List;

//每天免费10个
public class YunLianProxyPlugin extends TrialProxyPlugin {
    private static String indexUrl = "http://http.yunlianip.com/";
    private static String getProxyUrl = "http://t.11jsq.com/index.php/api/entry?method=proxyServer.generate_api_url&packid=1&fa=0&fetch_key=&qty=1&time=1&pro=&city=&port=1&format=txt&ss=1&css=&dt=1&specialTxt=3&specialJson=";
    private List<LoginParam> loginParamList = null;

    @Override
    public String getProxyPluginName() {
        return "YunLianProxyPlugin";
    }

    @Override
    public List<LoginParam> loginParamList() {
        return loginParamList;
    }

    public YunLianProxyPlugin() {
        loginParamList = new ArrayList<>();
        LoginParam param1 = new LoginParam("holiday555", "m13354612723", "13354612723");
        loginParamList.add(param1);
        LoginParam param2 = new LoginParam("happyone", "happy123", "18342238909");
        loginParamList.add(param2);
        LoginParam param3 = new LoginParam("wangheng", "heng123", "17783130253");
        loginParamList.add(param3);
        LoginParam param4 = new LoginParam("xiaozhu_177", "ty2571", "17704255028");
        loginParamList.add(param4);
        LoginParam param5 = new LoginParam("xiaozhu_177", "ty2571", "17704255028");
        loginParamList.add(param5);
    }


    @Override
    public ProxyObj process(LoginParam param) {
        return null;
    }

    @Override
    public CheckCookieBean checkCookieBean() {
        return null;
    }

    @Override
    public void login(LoginParam param) {

    }
}
