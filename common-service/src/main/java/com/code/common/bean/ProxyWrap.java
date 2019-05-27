package com.code.common.bean;

import com.code.common.exception.CodeException;
import com.code.common.proxy.TrialProxyPlugin;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProxyWrap implements Comparable {
    private TrialProxyPlugin plugin;
    private Integer order;
    private Boolean inValid = false;

    public ProxyWrap(TrialProxyPlugin plugin, Integer order) {
        this.plugin = plugin;
        this.order = order;
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof ProxyWrap))
            throw new CodeException("添加的对象不是proxyWrap");
        ProxyWrap proxyWrap = (ProxyWrap) o;

        //当compare返回0时，treeSet会认为是重复的对象.
        if (this.order.compareTo(proxyWrap.order) == 0) {
            if (this.equals(proxyWrap)) {
                return 0;
            }
            return 1;
        }
        return proxyWrap.order.compareTo(this.order);
    }

}