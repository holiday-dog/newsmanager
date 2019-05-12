package com.code.common.proxyplugin;

import com.code.common.bean.ProxyObj;
import com.code.common.proxy.ProxyPlugin;

import java.util.List;

public class JiSuProxyPlugin extends ProxyPlugin {
    private final String indexUrl = "http://www.superfastip.com/";

    @Override
    public String process() {
        return null;
    }

    @Override
    public List<ProxyObj> resolveProxys(String page) {
        return null;
    }
}
