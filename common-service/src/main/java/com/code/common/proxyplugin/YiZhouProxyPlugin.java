package com.code.common.proxyplugin;

import com.code.common.bean.CheckCookieBean;
import com.code.common.bean.LoginParam;
import com.code.common.bean.ProxyObj;
import com.code.common.crawl.WebResponse;
import com.code.common.proxy.TrialProxyPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

//每天200(但代理可用率不高，需要检测代理活性)
public class YiZhouProxyPlugin extends TrialProxyPlugin {
    private static String indexUrl = "https://www.ueuz.com/getip";
    private static String getProxyUrl1 = "https://too.ueuz.com/frontapi/public/http/get_ip/index?type=2918&iptimelong=1&ipcount=1&protocol=0&areatype=1&area=&resulttype=txt&duplicate=0&separator=1&other=&show_city=0&show_carrier=0&show_expire=0&isp=-1&auth_key=6bc8c3b739b02180100f3be3d367a77b&app_key=fac8d3f024a3d6ff113c5ee9334c2dca&timestamp=1557671560&sign=5409720EDBE1BCD0217F2ED2621A035D";
    private List<LoginParam> loginParamList = null;
    private Logger logger = LoggerFactory.getLogger(YiZhouProxyPlugin.class);
    private String cookies = null;

    @Override
    public String getProxyPluginName() {
        return "YiZhouProxyPlugin";
    }

    @Override
    public List<LoginParam> loginParamList() {
        return loginParamList;
    }

    public YiZhouProxyPlugin() {
        loginParamList = new ArrayList<>();
        LoginParam param1 = new LoginParam("holiday321", "m13354612723", "13354612723");
        loginParamList.add(param1);
    }


    @Override
    public ProxyObj process() {
        String packId = "9526";
        WebResponse response = null;
        ProxyObj proxyStr = null;
//        try {
//            WebRequest request = new WebRequest(String.format(getProxyUrl, packId));
//            String redisCookie = genCookie();
//            request.setCookie(StringUtils.isEmpty(redisCookie) ? cookies : redisCookie);
//            response = WebUtils.defaultClient().execute(request);
//        } catch (IOException e) {
//            logger.error("webclient error:{}", e);
//        }
//        String page = response.getRespText();
//        if (StringUtils.isNotEmpty(page) && !page.contains("\"success\":false")) {
//            page = page.trim();
//            logger.info("{} acquire proxyplugin:{}", getProxyPluginName(), page);
//            proxyStr = new ProxyStr(page.split(":")[0], Integer.parseInt(page.split(":")[1]));
//        } else {
//            logger.error("{} get proxyplugin error:{}", getProxyPluginName(), page);
//        }
        return proxyStr;
    }

    @Override
    public CheckCookieBean checkCookieBean() {
        return null;
    }

    @Override
    public void login(LoginParam param) {

    }

}
