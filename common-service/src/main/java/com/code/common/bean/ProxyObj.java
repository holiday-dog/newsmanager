package com.code.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProxyObj {
    private String proxyHost;
    private Integer proxyPort;
    private Integer timeOut;

    public ProxyObj(String proxyHost, Integer proxyPort) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }
//    public ProxyStr(String proxyplugin) {
//        this.proxyHost = proxyHost;
//        this.proxyPort = proxyPort;
//    }
}
