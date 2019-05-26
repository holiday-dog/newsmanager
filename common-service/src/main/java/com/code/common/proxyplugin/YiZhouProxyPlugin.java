package com.code.common.proxyplugin;

import com.code.common.bean.CheckCookieBean;
import com.code.common.bean.LoginParam;
import com.code.common.bean.ProxyObj;
import com.code.common.crawl.RequestMethod;
import com.code.common.crawl.WebClient;
import com.code.common.crawl.WebRequest;
import com.code.common.crawl.WebResponse;
import com.code.common.proxy.TrialProxyPlugin;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//每天免费20个,注册当天免费200个(有图片验证码，无法登陆)
public class YiZhouProxyPlugin extends TrialProxyPlugin {
    private static String indexUrl = "https://www.ueuz.com/getip";
    private static String getProxyUrl = "https://too.ueuz.com/frontapi/public/http/get_ip/index?type=%s&iptimelong=1&ipcount=1&protocol=0&areatype=1&area=&resulttype=txt&duplicate=1&separator=1&other=&show_city=0&show_carrier=0&show_expire=0&isp=-1&%s";
    private static List<LoginParam> loginParamList = new ArrayList<>();
    private Logger logger = LoggerFactory.getLogger(YiZhouProxyPlugin.class);
    private static WebClient client = WebClient.buildDefaultClient().build();

    @Override
    public String getProxyPluginName() {
        return "YiZhouProxyPlugin";
    }

    @Override
    public List<LoginParam> loginParamList() {
        return loginParamList;
    }

    static {
        LoginParam param1 = new LoginParam("holiday321", "m13354612723", "13354612723");
        param1.setProxyUserId("3159");
        param1.setProxyUrl("timestamp=1558843028&sign=CE46E6577E0AB68DB6EF259A95C6E97B");
        param1.setOthers("auth_key=05597c0df9014def85a269051fa23bfe&app_key=fac8d3f024a3d6ff113c5ee9334c2dca&timestamp=1558783870&sign=64A15C90CC59AB27277AF547C96796B8");
        loginParamList.add(param1);

        LoginParam param2 = new LoginParam("dawang", "dawang123", "18958078576");
        param2.setProxyUserId("3160");
        param2.setProxyUrl("timestamp=1558784290&sign=B4151E36B54D3D6189AAD318E91D32FB");
        param2.setOthers("auth_key=c210e08361581ec1492ff087baf7e5ef&app_key=3e13c9f643413f76802ff29d43b7ee22&timestamp=1558784290&sign=53159BB336615FDEA36BD2F180A0EDF1");
        loginParamList.add(param2);

        LoginParam param3 = new LoginParam("zhulihui", "zlh2937", "15754335612");
        param3.setProxyUserId("3169");
        param3.setProxyUrl("timestamp=1558843269&sign=741E896DF242DF9D34546922444BB80E");
        param3.setOthers("auth_key=bcf3d856b2a991d477cdad20fa62277b&app_key=4000e58a6b8e728e1162a735e61ba2d7&timestamp=1558843394&sign=777A50F7834363A980BA07E210A682AB");
        loginParamList.add(param3);
    }

    public YiZhouProxyPlugin() {
    }

    @Override
    public ProxyObj process(LoginParam param) {
        ProxyObj obj = null;
        try {
            String proxyUrl = String.format(getProxyUrl, param.getProxyUserId(), param.getOthers());
            WebRequest request = new WebRequest(proxyUrl);
            WebResponse response = client.execute(request);
            logger.info("getproxy page:{}", response.getRespText());
            if (StringUtils.isNotEmpty(response.getRespText())) {
                String proxys = response.getRespText().trim();
                obj = new ProxyObj(proxys.split(":")[0], Integer.parseInt(proxys.split(":")[1]));
            } else {
                logger.error("{} request proxy fail, page:{}", getProxyPluginName(), response.getRespText());
            }
        } catch (IOException e) {
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
            //获取每天的免费ip
            //{"code":0,"msg":"","data":[{"IpValidTimeZoneType":1,"IpCanUseQtyPerDay":10,"TodayUseIpQty":0,"surplusCanUseQty":10,"tiquQty":10,"subject":"1-5\u5206\u949f","fa":0,"ZoneValidTime":"2019-05-23 00:21:29"}]}
            WebRequest request1 = new WebRequest("https://too.ueuz.com/frontapi/public/http/get_ip/createurl");
            request1.setMethod(RequestMethod.POST_STRING);
            client.execute(request1);
            logger.info("获取每天的每天的免费ip成功");

            request1 = new WebRequest("https://too.ueuz.com/frontapi/public/http/get_ip/freeIp?" + param.getProxyUserId());
            request1.setMethod(RequestMethod.POST_STRING);
            client.execute(request1);
        } catch (Exception e) {
            logger.error("{} error, msg:{}", getProxyPluginName(), e);
        }
    }
}
