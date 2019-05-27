package com.code.spider.plugin;

import com.code.common.crawl.WebClient;
import com.code.common.crawl.WebRequest;
import com.code.common.crawl.WebResponse;
import com.code.common.enums.Modules;
import com.code.common.enums.ProcessStatus;
import com.code.common.utils.DateUtils;
import com.code.common.utils.JsoupUtils;
import com.code.spider.bean.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XinhuaEduPlugin extends ClientPlugin {
    private static String indexUrl = "http://education.news.cn/";
    private static final String eduListUrl = "http://qc.wa.news.cn/nodeart/list?nid=11109063&pgnum=%s&cnt=%s&tp=1&orderby=1?callback=jQuery112409162368214565164_%s&_=%s";
    private static final String topListUrl = "";
    private static WebClient client = WebClient.buildDefaultClient().build();
    private Logger logger = LoggerFactory.getLogger(XinhuaEduPlugin.class);

    @Override
    public String getClientPluginName() {
        return "Xinhua_Edu";
    }

    @Override
    Map<String, Object> preProcess(Map<String, Object> resultMap) {
        resultMap.put("spiderDate", LocalDateTime.now());
        resultMap.put("spiderIndexUrl", indexUrl);
        resultMap.put("spiderModule", Modules.EDUCATION);
        resultMap.put(Constants.STAGE, ProcessStatus.SPIDER_INIT);

        return resultMap;
    }

    @Override
    Map<String, Object> process(Map<String, Object> resultMap) throws IOException {
        WebRequest request = null;
        WebResponse response = null;
        //热点新闻
        request = new WebRequest("http://education.news.cn/");
        response = client.execute(request);

//        if (StringUtils.isNotEmpty(response.getRespText())) {
//            List<String> topEduUrlList = JsoupUtils.getAttr(response.getRespText(), "ul#gd_content li a", "href");
//            if (!CollectionUtils.isEmpty(topEduUrlList)) {
//                for (String topEduUrl : topEduUrlList) {
//                    System.out.println("----------------------");
//                    request = new WebRequest(topEduUrl);
//                    response = client.execute(request);
//                    System.out.println(response.getRespText());
//                }
//            }
//        }

        List<String> eduPageList = new ArrayList<>();
        long ts = DateUtils.nowTimeStamp();
        for (int i = 0; i < 1; i++) {
            String spiderUrl = String.format(eduListUrl, i, Constants.spiderPageNum, ts, ts);
            request = new WebRequest(spiderUrl);
            response = client.execute(request);
            System.out.println("-----------------");
            System.out.println(response.getRespText());

            eduPageList.add(response.getRespText());
        }
//        resultMap.put("eduPageList", eduPageList);

        return resultMap;
    }

    @Override
    Map<String, Object> handleData(Map<String, Object> resultMap) {
        for (String key : resultMap.keySet()) {
//            System.out.println(key + ":" + resultMap);
        }

        return resultMap;
    }
}
