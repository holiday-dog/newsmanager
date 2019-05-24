package com.code.common.proxyplugin;

import com.code.common.bean.ProxyObj;
import com.code.common.proxy.ProxyPlugin;

import java.util.List;
@Deprecated
public class QiYunProxyPlugin extends ProxyPlugin {
    private static String indexUrl = "http://www.qydaili.com/free/";

    @Override
    public String process() {
        return null;
    }

    @Override
    public List<ProxyObj> resolveProxys(String page) {
        return null;
    }
}
