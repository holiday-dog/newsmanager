package com.code.common.proxy;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProxyStr {
    private String proxyHost;
    private Integer proxyPort;
    private Integer timeOut;

    public ProxyStr(String proxyHost, Integer proxyPort) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
    }
}
