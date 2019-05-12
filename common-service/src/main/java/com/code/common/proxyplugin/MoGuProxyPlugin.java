package com.code.common.proxyplugin;

import com.code.common.bean.CheckCookieBean;
import com.code.common.bean.LoginParam;
import com.code.common.bean.ProxyObj;
import com.code.common.proxy.TrialProxyPlugin;

import java.util.ArrayList;
import java.util.List;

//免费10个，但登录是滑块，较难处理,暂时不用
@Deprecated
public class MoGuProxyPlugin extends TrialProxyPlugin {
    private static String indexUrl = "http://www.moguproxy.com/";
    private List<LoginParam> loginParamList = null;

    @Override
    public String getProxyPluginName() {
        return "MoGuProxyPlugin";
    }

    @Override
    public List<LoginParam> loginParamList() {
        return loginParamList;
    }

    public MoGuProxyPlugin() {
        loginParamList = new ArrayList<>();
        LoginParam param1 = new LoginParam("13354612723", "m13354612723", "13354612723");
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
