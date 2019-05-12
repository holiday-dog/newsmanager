package com.code.common.proxy;

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

    public static ProxyObj getProxy() {
        ProxyObj proxy = null;

//        (proxyplugin==null){
//            obtainProxys();
//        }
        return proxy;
    }

    public static void obtainProxys() {
//        List<Class> proxyPluginClasses = getPluginClasses();

    }

    public static List<Class> getPluginClasses() throws IOException, ClassNotFoundException {
        List<Class> classes = new ArrayList<>();

        URLClassLoader classLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
        URL packUrl = classLoader.getResource(packageName);
        URL[] urls = new URL[]{packUrl};
        classLoader = new URLClassLoader(urls);

        for (File file : new File(packUrl.getPath()).listFiles()) {
            String className = packageName.replace("/", ".") + StringUtils.substring(file.getName(), 0, file.getName().lastIndexOf("."));
            Class cls = classLoader.loadClass(className);
            classes.add(cls);
        }

        return classes;
    }
}
