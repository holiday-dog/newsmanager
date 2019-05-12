package com.code.common.proxyplugin;

import com.code.common.bean.CheckCookieBean;
import com.code.common.bean.LoginParam;
import com.code.common.bean.ProxyObj;
import com.code.common.proxy.TrialProxyPlugin;

import java.util.ArrayList;
import java.util.List;

//每天免费20个
public class WanDouProxyPlugin extends TrialProxyPlugin {
    private static final String indexUrl = "https://www.wandouip.com/";
    private static String getProxyUrl = "http://api.wandoudl.com/api/ip?app_key=83eb53ac2ce277a1f336cdc582bc8c19&pack=0&num=1&xy=1&type=2&lb=\\r\\n&mr=2&";
    private List<LoginParam> loginParamList = null;

    @Override
    public String getProxyPluginName() {
        return "WanDouProxyPlugin";
    }

    public WanDouProxyPlugin() {
        loginParamList = new ArrayList<>();
        LoginParam param1 = new LoginParam("m13354612723@163.com", "Mm13354612723", "13354612723");
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
