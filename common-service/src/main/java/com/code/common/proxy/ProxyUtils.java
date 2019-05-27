package com.code.common.proxy;

import com.code.common.annos.ProxyOrder;
import com.code.common.bean.ProxyObj;
import com.code.common.proxyplugin.JiGuangProxyPlugin;
import com.code.common.proxyplugin.MoGuProxyPlugin;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

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
    private static Class[] endProxy = {MoGuProxyPlugin.class, JiGuangProxyPlugin.class};
    private static Logger logger = LoggerFactory.getLogger(ProxyUtils.class);

    //YiZhouProxyPlugin需要登录成功的一个参数
    public static ProxyObj getProxy() {
        ProxyObj proxy = null;

        try {
            if (CollectionUtils.isEmpty(avaliableProxy) && CollectionUtils.isEmpty(poorProxy)) {
                logger.info("首次获取代理，需要初始化代理插件list");
                scanPlugin();
            }
//        (proxyplugin == null) {
//            obtainProxys();
//        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return proxy;
    }

    //最终高可用代理插件(拥有大量代理，但是可用时间较短)
    public static ProxyObj getEndProxy() {
        ProxyObj obj = null;
        try {
            for (Class clsPlugin : endProxy) {
                TrialProxyPlugin proxyPlugin = (TrialProxyPlugin) clsPlugin.newInstance();
                obj = proxyPlugin.getProxy();
                if (obj != null) {
                    return obj;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
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
                if (cls.getAnnotation(ProxyOrder.class) == null) {
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
