package com.code.common.proxyplugin;

import com.code.common.bean.CheckCookieBean;
import com.code.common.bean.LoginParam;
import com.code.common.bean.ProxyObj;
import com.code.common.proxy.TrialProxyPlugin;

import java.util.ArrayList;
import java.util.List;

//每天免费20个
public class ZhiMaProxyPlugin extends TrialProxyPlugin {
    private static String indexUrl = "http://www.zhimaruanjian.com/";
    private static String getProxyUrl = "http://webapi.http.zhimacangku.com/getip?num=1&type=1&pro=&city=0&yys=0&port=1&pack=52239&ts=0&ys=0&cs=0&lb=1&sb=0&pb=45&mr=1&regions=";
    private List<LoginParam> loginParamList = null;

    @Override
    public String getProxyPluginName() {
        return "ZhiMaProxyPlugin";
    }

    public ZhiMaProxyPlugin() {
        loginParamList = new ArrayList<>();
        LoginParam param1 = new LoginParam("holiday123", "m13354612723", "13354612723");
        loginParamList.add(param1);
    }

    @Override
    public List<LoginParam> loginParamList() {
        return loginParamList;
    }

    @Override
    public ProxyObj process() {
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
