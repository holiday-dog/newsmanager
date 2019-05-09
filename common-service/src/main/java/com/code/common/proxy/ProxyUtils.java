package com.code.common.proxy;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class ProxyUtils {
    private final static String packageName = "com/code/common/proxy/";

    public static ProxyStr getProxy() {
        ProxyStr proxy = null;

//        (proxy==null){
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
