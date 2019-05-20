package com.code.common.proxy;

import com.code.common.bean.CheckCookieBean;
import com.code.common.bean.LoginParam;
import com.code.common.bean.ProxyObj;

import java.util.List;

//不可用代理插件
public abstract class NonTrialProxyPlugin extends TrialProxyPlugin {
    private List<LoginParam> loginParamList = null;

    @Override
    public String getProxyPluginName() {
        return "JingLingTrialProxyPlugin";
    }

//    @Override
//    public List<LoginParam> loginParamList() {
//        return loginParamList;
//    }

//    public NonTrialProxyPlugin() {
//        loginParamList = new ArrayList<>();
//        LoginParam param1 = new LoginParam("holidaydog", "m13354612723", "13354612723");
//        loginParamList.add(param1);
//    }

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
