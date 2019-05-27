package com.code.common.proxyplugin;

import com.code.common.annos.ProxyOrder;
import com.code.common.bean.CheckCookieBean;
import com.code.common.bean.LoginParam;
import com.code.common.bean.ProxyObj;
import com.code.common.crawl.RequestMethod;
import com.code.common.crawl.WebClient;
import com.code.common.crawl.WebRequest;
import com.code.common.crawl.WebResponse;
import com.code.common.proxy.TrialProxyPlugin;
import com.code.common.utils.JsonPathUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

//每天免费20个,注册当天免费200个(有图片验证码，无法登陆)
@ProxyOrder(order = 80)
public class YiZhouProxyPlugin extends TrialProxyPlugin {
    private static String indexUrl = "https://www.ueuz.com/getip";
    private static String getProxyUrl = null;
    private static List<LoginParam> loginParamList = new ArrayList<>();
    private static Logger logger = LoggerFactory.getLogger(YiZhouProxyPlugin.class);
    private static WebClient client = WebClient.buildDefaultClient().build();
    private static ConcurrentHashMap<String, String> cookies = new ConcurrentHashMap<>();

    //https://too.ueuz.com/frontapi/public/http/get_ip/index?type=3174&iptimelong=1&ipcount=1&protocol=0&areatype=1&area=&resulttype=txt&duplicate=0&separator=1&other=&show_city=0&show_carrier=0&show_expire=0&isp=-1&auth_key=ea5adc375a93d1cdc8c83259d66f579b&app_key=3e13c9f643413f76802ff29d43b7ee22&timestamp=1558876899&sign=F1C49FF0E2006E64E69F4E384FB4CFDE
    @Override
    public String getProxyPluginName() {
        return "YiZhouProxyPlugin";
    }

    @Override
    public List<LoginParam> loginParamList() {
        return loginParamList;
    }

    static {
//        LoginParam param1 = new LoginParam("holiday321", "m13354612723", "13354612723");
//        param1.setProxyUserId("2d9a161c14d64849309ef1fd070933c9");
//        loginParamList.add(param1);
//        LoginParam param2 = new LoginParam("dawang", "dawang123", "18958078576");
//        param2.setProxyUserId("8727aa46c38e9b05d485d293292a4194");//登录标记Authorization
//        loginParamList.add(param2);
        LoginParam param3 = new LoginParam("zhulihui", "zlh2937", "15754335612");
        param3.setProxyUserId("6d0fdaf927c4f4736cefd084e577226b");
        loginParamList.add(param3);
    }

    public YiZhouProxyPlugin() {
    }

    @Override
    public ProxyObj process(LoginParam param) {
        ProxyObj obj = null;
        try {
            if (StringUtils.isEmpty(cookies.get(param.getUsername()))) {
                getFreeIp(param);
            }
            WebRequest request = new WebRequest(cookies.get(param.getUsername()));
            WebResponse response = client.execute(request);
            if (response.getRespText().contains("用户未登录")) {
                getFreeIp(param);
                request = new WebRequest(cookies.get(param.getUsername()));
                response = client.execute(request);
            }
            logger.info("getproxy page:{}", response.getRespText());
            if (StringUtils.isNotEmpty(response.getRespText())) {
                String proxys = response.getRespText().trim();
                obj = new ProxyObj(proxys.split(":")[0], Integer.parseInt(proxys.split(":")[1]));
            } else {
                logger.error("{} request proxy fail, page:{}", getProxyPluginName(), response.getRespText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public CheckCookieBean checkCookieBean(LoginParam param) {
        return null;
    }

    @Override
    public void getFreeIp(LoginParam param) {
        try {
            Long ts = new Date().getTime();
            //获取每天的免费ip
            WebRequest request1 = new WebRequest("https://too.ueuz.com/frontapi/public/http/get_ip/freeIp?timestamp=" + ts + "&sign=E35BC0CBA82BFD9351B7D432B2BBA7D8");
            request1.setMethod(RequestMethod.POST_STRING);
            request1.addRequestHeader("Authorization", param.getProxyUserId());
            request1.setCookie(param.getCookie());
            WebResponse response = client.execute(request1);
            logger.info("获取每天的每天的免费ip成功, page:{}", response.getRespText());
//
            request1 = new WebRequest("https://too.ueuz.com/frontapi/public/http/get_ip/info?timestamp=" + ts + "&sign=FD5093FE0579C15C82B8E1C9CF415122");
            request1.addRequestHeader("Authorization", param.getProxyUserId());
            response = client.execute(request1);
            Object type = JsonPathUtils.getValue(response.getRespText(), "$.data.packagelist[0].id");

            request1 = new WebRequest("https://too.ueuz.com/frontapi/public/http/get_ip/createurl");
            request1.setMethod(RequestMethod.POST_STRING);
            request1.addRequestHeader("Authorization", param.getProxyUserId());
            request1.setRequestBodyString("type=" + type + "&ipcount=1&iptimelong=1&protocol=0&areatype=1&area=&resulttype=txt&separator=1&other=&duplicate=0&show_city=0&show_carrier=0&show_expire=0&isp=-1&timestamp=" + ts + "&sign=EC6FF6E189E3B755F7B28A4EC491045D");
            response = client.execute(request1);
            logger.info("获取proxyUrl页面:{}", response.getRespText());
            getProxyUrl = (String) JsonPathUtils.getValue(response.getRespText(), "$.data.url");
            cookies.put(param.getUsername(), getProxyUrl);
        } catch (Exception e) {
            logger.error("{} error, msg:{}", getProxyPluginName(), e);
        }
    }
}
