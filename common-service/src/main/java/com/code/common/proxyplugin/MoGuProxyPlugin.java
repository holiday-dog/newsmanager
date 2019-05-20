package com.code.common.proxyplugin;

import com.code.common.bean.LoginParam;
import com.code.common.proxy.NonTrialProxyPlugin;

import java.util.ArrayList;
import java.util.List;

//免费500个，但是只有1个小时的测试时间，点击测试私密代理会有但登录是滑块
//@Deprecated
public class MoGuProxyPlugin extends NonTrialProxyPlugin {
    private static String indexUrl = "http://www.moguproxy.com/";
    private List<LoginParam> loginParamList = null;

    @Override
    public List<LoginParam> loginParamList() {
        return loginParamList;
    }

    public MoGuProxyPlugin() {
        loginParamList = new ArrayList<>();
        LoginParam param1 = new LoginParam("18958078575", "qwe189", "13354612723");
        loginParamList.add(param1);
        LoginParam param2 = new LoginParam("17783130253", "xysdwf123", "17783130253");
        loginParamList.add(param2);
        LoginParam param3 = new LoginParam("17704255028", "whf8923", "17783130253");
        loginParamList.add(param3);
//        LoginParam param4 = new LoginParam("17783130253", "xysdwf123", "17783130253");
//        loginParamList.add(param4);
//        LoginParam param5 = new LoginParam("17783130253", "xysdwf123", "17783130253");
//        loginParamList.add(param5);
    }

}
