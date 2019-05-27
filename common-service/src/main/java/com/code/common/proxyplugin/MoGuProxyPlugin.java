package com.code.common.proxyplugin;

import com.code.common.bean.LoginParam;
import com.code.common.bean.ProxyObj;
import com.code.common.crawl.WebClient;
import com.code.common.crawl.WebRequest;
import com.code.common.crawl.WebResponse;
import com.code.common.proxy.NonTrialProxyPlugin;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

//免费500个，但是只有1个小时的测试时间
@Deprecated
public class MoGuProxyPlugin extends NonTrialProxyPlugin {
    private static String indexUrl = "http://www.moguproxy.com/";
    private static List<LoginParam> loginParamList = null;
    private static String getProxyUrl = null;
    private static WebClient client = WebClient.buildDefaultClient().build();
    private static Logger logger = LoggerFactory.getLogger(MoGuProxyPlugin.class);

    @Override
    public List<LoginParam> loginParamList() {
        return loginParamList;
    }

    public MoGuProxyPlugin() {
        loginParamList = new ArrayList<>();
        LoginParam param1 = new LoginParam("18958078575", "qwe189", "13354612723");
        loginParamList.add(param1);
        LoginParam param2 = new LoginParam("17783130253", "xysdwf123", "17783130253");
        loginParamList.add(param2);
        LoginParam param3 = new LoginParam("17704255028", "whf8923", "17783130253");
        loginParamList.add(param3);
    }

    @Override
    public ProxyObj getProxy() {
        ProxyObj obj = null;
        try {
            WebRequest request = new WebRequest(getProxyUrl);
            WebResponse response = client.execute(request);
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
}
