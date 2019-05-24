package com.code.common.proxyplugin;

import com.code.common.bean.CheckCookieBean;
import com.code.common.bean.LoginParam;
import com.code.common.bean.ProxyObj;
import com.code.common.proxy.TrialProxyPlugin;

import java.util.ArrayList;
import java.util.List;

//每天免费20个(cookie可以2小时不过期)
public class TuZiProxyPlugin extends TrialProxyPlugin {
    private static String indexUrl = "https://vip.tuziip.com/crawler";
    private static String getProxyUrl = "http://t.11jsq.com/index.php/api/entry?method=proxyServer.generate_api_url&packid=1&fa=0&fetch_key=&qty=1&time=1&pro=&city=&port=1&format=txt&ss=1&css=&dt=1&specialTxt=3&specialJson=";
    private List<LoginParam> loginParamList = null;

    @Override
    public String getProxyPluginName() {
        return "TuZiProxyPlugin";
    }

    @Override
    public List<LoginParam> loginParamList() {
        return loginParamList;
    }

    public TuZiProxyPlugin() {
        loginParamList = new ArrayList<>();
        LoginParam param1 = new LoginParam("holiday321", "199711", "13354612723");
        loginParamList.add(param1);
        LoginParam param2 = new LoginParam("xuwenqiang", "xwq123", "18342238909");
        loginParamList.add(param2);
        LoginParam param3 = new LoginParam("luckcat", "catcat", "17783130253");
        loginParamList.add(param3);
        LoginParam param4 = new LoginParam("admin8575", "271543", "18958078575");
        loginParamList.add(param4);
//        LoginParam param5 = new LoginParam("xuwenqiang", "xwq123", "18342238909");
//        loginParamList.add(param5);
        LoginParam param6 = new LoginParam("feitianzai", "042371", "17704255028");
        loginParamList.add(param6);
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
