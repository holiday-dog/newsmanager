package com.code.common.proxyplugin;

import com.code.common.bean.CheckCookieBean;
import com.code.common.bean.LoginParam;
import com.code.common.bean.ProxyObj;
import com.code.common.crawl.RequestMethod;
import com.code.common.crawl.WebClient;
import com.code.common.crawl.WebRequest;
import com.code.common.crawl.WebResponse;
import com.code.common.proxy.TrialProxyPlugin;
import com.code.common.utils.PatternUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//每天免费10(有图片验证码，无法进行登录，只能存cookie)
public class HeiDongProxyPlugin extends TrialProxyPlugin {
    private static String indexUrl = "http://http.hunbovps.com/getapi.html";
    private static String getProxyUrl = "http://ip.ipjldl.com/index.php/api/entry?method=proxyServer.hdtiqu_api_url&packid=1&fa=0&dt=0&fetch_key=&qty=1&time=100&port=1&format=txt&ss=1&css=&pro=&city=";
    private static List<LoginParam> loginParamList = new ArrayList<>();
    private static Logger logger = LoggerFactory.getLogger(HeiDongProxyPlugin.class);
    private WebClient client = WebClient.buildDefaultClient().build();

    @Override
    public String getProxyPluginName() {
        return "HeiDongProxyPlugin";
    }

    @Override
    public List<LoginParam> loginParamList() {
        return loginParamList;
    }

    static {
        LoginParam param1 = new LoginParam("holiday111", "m13354612723", "13354612723");
        param1.setCookie("__qidianid=6f21afaa21f16f1ead0c479eaa4a84093f2c31a8; pgv_info=ssid=s293699408; pgv_pvid=7158446990; ts_uid=7688500472; ts_refer=http.hunbovps.com/Users-login.html; ts_last=webpage.qidian.qq.com/2/chat/pc/index.html");
        loginParamList.add(param1);
        LoginParam param2 = new LoginParam("author_wang", "wang1769", "18958078576");
        param2.setCookie("Hm_lvt_1185efb26623ba3dcfa0310036990dd5=1558448073,1558853391,1558853824; __root_domain_v=.hunbovps.com; _qddaz=QD.6x4s1x.uoa85r.jvxvqti5; _qdda=3-1.1f64ow; _qddamta_2852157335=3-0; PHPSESSID=29e3dq0vsmfneol61o7d4nted0; Hm_lpvt_1185efb26623ba3dcfa0310036990dd5=1558854053; _qddab=3-swhcs3.jw4lbh14");
        loginParamList.add(param2);
        LoginParam param3 = new LoginParam("zhiqiang", "zhao123", "18342238909");
        param3.setCookie("Hm_lvt_1185efb26623ba3dcfa0310036990dd5=1558448073,1558853391; __root_domain_v=.hunbovps.com; _qddaz=QD.6x4s1x.uoa85r.jvxvqti5; PHPSESSID=1sfp5rgmuilvdet2jck869paj3; Hm_lpvt_1185efb26623ba3dcfa0310036990dd5=1558853424; _qdda=3-1.1; _qddab=3-tu33xz.jw4l26ni; _qddamta_2852157335=3-0");
        loginParamList.add(param3);
    }

    public HeiDongProxyPlugin() {
    }

    @Override
    public ProxyObj process(LoginParam param) {
        ProxyObj obj = null;
        WebRequest request = new WebRequest(getProxyUrl);
        request.setCookie(param.getCookie());
        try {
            WebResponse response = client.execute(request);
            logger.info("getproxy page:{}", response.getRespText());
            if (response.getRespText().contains("\"code\":5")) {
                String cookie = client.getContextCookies();

                String proxy = PatternUtils.groupOne(response.getRespText(), "(\\d+\\.\\d+\\.\\d+\\.\\d+)", 1);
                //添加ip白名单
                request = new WebRequest("http://http.hunbovps.com/users-whiteIpAddNew.html?appid=288&appkey=c79a952384bcd0591f648d7901788434&whiteip=" + proxy);
                response = client.execute(request);
                logger.info("添加ip白名单之后的页面：{}", response.getRespText());
                //获取免费ip
                getFreeIp(param);

                request = new WebRequest(getProxyUrl);
                response = client.execute(request);
                logger.info("添加ip之后，获取代理得到的页面：{}", response.getRespText());
            }
            if (StringUtils.isNotEmpty(response.getRespText())) {
                String[] proxys = response.getRespText().trim().split(":");
                obj = new ProxyObj(proxys[0], Integer.parseInt(proxys[1]));
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
            WebRequest request1 = new WebRequest("http://http.hunbovps.com/Index-getFree.html");
            request1.setMethod(RequestMethod.POST_STRING);
            WebResponse response = client.execute(request1);
            logger.info("获取每天的每天的免费ip成功");

            request1 = new WebRequest("http://http.hunbovps.com/Index-getips.html");
            request1.setMethod(RequestMethod.POST_STRING);
            client.execute(request1);
        } catch (Exception e) {
            logger.error("{} error, msg:{}", getProxyPluginName(), e);
        }
    }

    @Override
    public String getCookie(String loginName) {
        for (LoginParam param : loginParamList) {
            if (loginName.equals(param.getUsername())) {
                return param.getCookie();
            }
        }
        return StringUtils.EMPTY;
    }
}
