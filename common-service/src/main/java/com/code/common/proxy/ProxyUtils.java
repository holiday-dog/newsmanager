package com.code.common.proxy;

import com.code.common.annos.ProxyOrder;
import com.code.common.bean.ProxyObj;
import com.code.common.bean.ProxyWrap;
import com.code.common.exception.CodeException;
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
import java.util.Set;
import java.util.TreeSet;

public class ProxyUtils {
    private final static String packageName = "com/code/common/proxyplugin/";
    private final static String recommendUrl = "http://dl.l4110.com/";//推荐ip代理的网站
    private static Set<ProxyWrap> proxyWraps;
    private static List<ProxyWrap> invalidWraps = new ArrayList<>();
    private static Class[] endProxy = {MoGuProxyPlugin.class, JiGuangProxyPlugin.class};
    private static Logger logger = LoggerFactory.getLogger(ProxyUtils.class);

    //YiZhouProxyPlugin需要登录成功的一个参数
    public static ProxyObj getProxy() {
        ProxyObj proxy = null;

        try {
            if (CollectionUtils.isEmpty(proxyWraps)) {
                logger.info("首次获取代理，需要初始化代理插件集合");
                scanPlugin();
            }
            if (CollectionUtils.isEmpty(proxyWraps)) {
                throw new CodeException("初始化代理插件集合失败");
            }

            for (ProxyWrap wrap : proxyWraps) {
                if (wrap.getInValid())
                    continue;
                int n = 0;
                while (proxy == null && n < 3) {
                    n++;
                    proxy = wrap.getPlugin().getProxy();
                }
                if (proxy != null) {
                    logger.info("{} acquire proxy success, proxy:{}", wrap, proxy);
                    break;
                } else if (n == 3 && proxy == null) {
                    logger.info("{} invalid", wrap);
                    wrap.setInValid(true);
                }
            }
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

    private static void scanPlugin() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        proxyWraps = new TreeSet<>();

        URLClassLoader classLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
        URL packUrl = classLoader.getResource(packageName);
        URL[] urls = new URL[]{packUrl};
        classLoader = new URLClassLoader(urls);

        for (File file : new File(packUrl.getPath()).listFiles()) {
            String className = packageName.replace("/", ".") + StringUtils.substring(file.getName(), 0, file.getName().lastIndexOf("."));
            Class cls = classLoader.loadClass(className);

            //获取可用插件
            if (cls.isAnnotationPresent(ProxyOrder.class) && !cls.isAnnotationPresent(Deprecated.class)) {
                ProxyOrder proxyOrder = (ProxyOrder) cls.getAnnotation(ProxyOrder.class);
                ProxyWrap wrap = new ProxyWrap((TrialProxyPlugin) cls.newInstance(), proxyOrder.order());
                proxyWraps.add(wrap);
            }
        }
    }


    //测试
    public static Set<ProxyWrap> getProxyPlugin() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        scanPlugin();
        return proxyWraps;
    }

}
