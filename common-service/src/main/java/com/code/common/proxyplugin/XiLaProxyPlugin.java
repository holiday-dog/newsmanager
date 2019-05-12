package com.code.common.proxyplugin;

import com.code.common.bean.CheckCookieBean;
import com.code.common.bean.LoginParam;
import com.code.common.bean.ProxyObj;
import com.code.common.proxy.TrialProxyPlugin;

import java.util.ArrayList;
import java.util.List;

//每天免费500个，但是可用率太低,获取ip重试5次
public class XiLaProxyPlugin extends TrialProxyPlugin {
    private final String indexUrl = "http://www.xiladaili.com/login/?next=/interface/";
    private static String getProxyUrl = "http://www.xiladaili.com/api/?uuid=472adb9ba1464f778350d58387f0c843&num=1&place=中国&protocol=0&sortby=0&repeat=1&format=3&position=1";
    private List<LoginParam> loginParamList = null;

    @Override
    public String getProxyPluginName() {
        return "XiLaProxyPlugin";
    }

    @Override
    public List<LoginParam> loginParamList() {
        return loginParamList;
    }

    public XiLaProxyPlugin() {
        loginParamList = new ArrayList<>();
        LoginParam param1 = new LoginParam("holiday999", "m13354612723", "13354612723");
        loginParamList.add(param1);
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
