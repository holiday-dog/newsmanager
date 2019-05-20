package com.code.common.proxyplugin;

import com.code.common.bean.CheckCookieBean;
import com.code.common.bean.LoginParam;
import com.code.common.bean.ProxyObj;
import com.code.common.proxy.TrialProxyPlugin;

import java.util.ArrayList;
import java.util.List;

//每天免费10个
public class HengXingProxyPlugin extends TrialProxyPlugin {
    private static final String indexUrl = "http://h.zbzok.com/";
    private static String getProxyUrl = "http://120.79.197.226:8080/Index-generate_api_url.html?packid=1&fa=0&qty=1&port=1&format=txt&ss=1&css=&pro=&city=";
    private List<LoginParam> loginParamList = null;

    @Override
    public String getProxyPluginName() {
        return "HengXingProxyPlugin";
    }

    @Override
    public List<LoginParam> loginParamList() {
        return loginParamList;
    }

    public HengXingProxyPlugin() {
        loginParamList = new ArrayList<>();
        LoginParam param1 = new LoginParam("holiday333", "m13354612723", "13354612723", "m13354612723@126.com");
        loginParamList.add(param1);
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
