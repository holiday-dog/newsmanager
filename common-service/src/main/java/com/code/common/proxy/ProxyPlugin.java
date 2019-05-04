package com.code.common.proxy;

import java.util.List;

public abstract class ProxyPlugin {

    public abstract List<ProxyStr> process();

    public List<ProxyStr> doProcess() {
        try{

        }catch (Exception e){

        }

        return process();
    }
}
