package com.code.common.proxyplugin;

import com.code.common.annos.ProxyOrder;
import com.code.common.bean.CheckCookieBean;
import com.code.common.bean.LoginParam;
import com.code.common.bean.ProxyObj;
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
import java.util.concurrent.TimeUnit;

//每天免费500个，但是可用率太低,获取ip重试5次
@ProxyOrder(order = 100)
public class XiLaProxyPlugin extends TrialProxyPlugin {
    private final String indexUrl = "http://www.xiladaili.com/login/?next=/interface/";
    private static String getProxyUrl = "http://www.xiladaili.com/api/?uuid=472adb9ba1464f778350d58387f0c843&num=2&place=中国&protocol=0&sortby=0&repeat=1&format=3&position=1";
    private List<LoginParam> loginParamList = null;
    private static WebClient client = WebClient.buildDefaultClient().build();
    private static Logger logger = LoggerFactory.getLogger(XiLaProxyPlugin.class);

    @Override
    public String getProxyPluginName() {
        return "XiLaProxyPlugin";
    }

    @Override
    public List<LoginParam> loginParamList() {
        return loginParamList;
    }

    public XiLaProxyPlugin() {
        loginParamList = new ArrayList<>();
        LoginParam param1 = new LoginParam("holiday999", "m13354612723", "13354612723");
        loginParamList.add(param1);
    }

    @Override
    public ProxyObj process(LoginParam param) {
        ProxyObj obj = null;
        try {
            for (int i = 0; i < 2; i++) {
                WebRequest request = new WebRequest(getProxyUrl);
                WebResponse response = client.execute(request);
                logger.info("getproxy page:{}", response.getRespText());
                if (response.getRespText().contains("调用频率过快")) {
                    TimeUnit.SECONDS.sleep(1);
                    logger.info("sleep 1s, retry request proxy");
                    continue;
                }
                if (StringUtils.isNotEmpty(response.getRespText())) {
                    String[] proxys = response.getRespText().split(" ");
                    for (int n = 0; n < 2; n++) {
                        String proxy = proxys[n].trim();
                        obj = new ProxyObj(proxy.split(":")[0], Integer.parseInt(proxy.split(":")[1]));
                        if (validateProxy(obj)) {
                            return obj;
                        }
                    }
                } else {
                    logger.error("{} request proxy fail, page:{}", getProxyPluginName(), response.getRespText());
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public Integer getProxyRetryCount() {
        return 5;
    }

//    @Override
//    public boolean needValidateProxy() {
//        return true;
//    }

    @Override
    public CheckCookieBean checkCookieBean(LoginParam param) {
        return null;
    }
}
