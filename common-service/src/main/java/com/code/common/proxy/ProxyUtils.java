package com.code.common.proxy;

import com.code.common.annos.PoorProxy;
import com.code.common.bean.ProxyObj;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class ProxyUtils {
    private final static String packageName = "com/code/common/proxyplugin/";
    private final static String recommendUrl = "http://dl.l4110.com/";//推荐ip代理的网站
    private static List<Class> avaliableProxy = null;
    private static List<Class> poorProxy = null;

    public static ProxyObj getProxy() {
        ProxyObj proxy = null;

//        (proxyplugin==null){
//            obtainProxys();
//        }
        return proxy;
    }

    private static void scanPlugin() throws IOException, ClassNotFoundException {
        avaliableProxy = new ArrayList<>();
        poorProxy = new ArrayList<>();

        URLClassLoader classLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
        URL packUrl = classLoader.getResource(packageName);
        URL[] urls = new URL[]{packUrl};
        classLoader = new URLClassLoader(urls);

        for (File file : new File(packUrl.getPath()).listFiles()) {
            String className = packageName.replace("/", ".") + StringUtils.substring(file.getName(), 0, file.getName().lastIndexOf("."));
            Class cls = classLoader.loadClass(className);

            //获取可用插件
            if (cls.getAnnotation(Deprecated.class) == null) {
                if (cls.getAnnotation(PoorProxy.class) == null) {
                    avaliableProxy.add(cls);
                } else {
                    poorProxy.add(cls);
                }
            }
        }
    }

    //测试
    public static List<Class> getProxyPlugin() throws IOException, ClassNotFoundException {
        scanPlugin();
//        return avaliableProxy;
        return poorProxy;
    }

}
