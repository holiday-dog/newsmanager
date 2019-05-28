package com.code.spider.plugin;

import com.alibaba.fastjson.JSON;
import com.code.common.bean.News;
import com.code.common.crawl.WebClient;
import com.code.common.crawl.WebRequest;
import com.code.common.crawl.WebResponse;
import com.code.common.enums.Modules;
import com.code.common.utils.DateUtils;
import com.code.common.utils.JsonPathUtils;
import com.code.common.utils.JsoupUtils;
import com.code.common.utils.PatternUtils;
import com.code.spider.bean.RawData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenminEduPlugin extends ClientPlugin {
    private static String indexUrl = "http://edu.people.com.cn/";
    private static WebClient client = WebClient.buildDefaultClient().buildRouteAndCount(50, 100).build();
    private Logger logger = LoggerFactory.getLogger(RenminEduPlugin.class);

    @Override
    public String getClientPluginName() {
        return "Xinhua_Travel_Plugin";
    }

    @Override
    Map<String, Object> preProcess(Map<String, Object> resultMap) {
        resultMap.put("spiderDate", LocalDateTime.now());
        resultMap.put("moduleType", Modules.EDUCATION);
        resultMap.put("spiderWebsite", "Renmin");

        return resultMap;
    }

    @Override
    Map<String, Object> process(Map<String, Object> resultMap) throws IOException {
        WebRequest request = null;
        WebResponse response = null;
        Map<String, Object> spiderData = new HashMap<>();
        request = new WebRequest("http://edu.people.com.cn/");
        response = client.execute(request);

        if (StringUtils.isNotEmpty(response.getRespText())) {

            List<String> newestEduUrlList = JsoupUtils.getAttr(response.getRespText(), "ul.newestList li a", "href");
            if (!CollectionUtils.isEmpty(newestEduUrlList)) {
                List<RawData> newestEduList = new ArrayList<>();
//                for (String newestEduUrl : newestEduUrlList) {
                for (int i = 0; i < 1; i++) {
                    String newestEduUrl = newestEduUrlList.get(i);
                    request = new WebRequest(newestEduUrl);
                    response = client.execute(request);
                    newestEduList.add(new RawData(newestEduUrl, response.getRespText()));
                }
                spiderData.put("newestEduList", newestEduList);
            }
        }
        //新闻历史

        long ts = DateUtils.nowTimeStamp();
        for (int i = 1; i <= 1; i++) {
//            String spiderUrl = String.format(eduListUrl, i, Constants.spiderPageNum, ts, ts);
//            request = new WebRequest(spiderUrl);
//            response = client.execute(request);

            String page = PatternUtils.groupOne(response.getRespText(), "jQuery\\d+_\\d+\\(([^\\(\\)]+)\\)", 1);
            List<String> linkUrls = JsonPathUtils.getValueList(page, "$.data.list[*].LinkUrl");
            if (!CollectionUtils.isEmpty(linkUrls)) {
                List<RawData> eduPageList = new ArrayList<>();
                for (int j = 0; j < 2; j++) {
                    request = new WebRequest(linkUrls.get(j));
                    response = client.execute(request);
                    eduPageList.add(new RawData(linkUrls.get(j), response.getRespText()));
                }
                spiderData.put("historyEduList", eduPageList);
            }
        }


        return spiderData;
    }

    @Override
    Map<String, Object> handleData(Map<String, Object> spiderData) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            for (String key : spiderData.keySet()) {
                logger.info("handler {} data", key);
                List<News> newsList = new ArrayList<>();
                List<RawData> results = (List<RawData>) spiderData.get(key);
                if (CollectionUtils.isEmpty(results)) {
                    continue;
                }
                for (RawData result : results) {
                    String page = result.getPage();
                    if (page.contains("下一页") && page.contains("div_currpage")) {
                        newsList.add(handleMultiPage(page, result.getUrl()));
                    } else {
                        newsList.add(handleSinglePage(page, result.getUrl()));
                    }
                }
                resultMap.put(key, newsList);
            }
        } catch (Exception e) {
        }
        System.out.println(JSON.toJSONString(resultMap));

        return resultMap;
    }

    private News handleSinglePage(String page, String url) {
        News news = ExtractorUtils.extractXinhua(page);
        news.setContent(ExtractorUtils.extractorXinhuaContent(page, url));
        news.setReferUrl(url);
        return news;
    }

    public News handleMultiPage(String page, String url) throws IOException {
        News news = ExtractorUtils.extractXinhua(page);
        String content = ExtractorUtils.extractorXinhuaContent(page, url);

        StringBuffer contentBuffer = new StringBuffer(content);
        String nextPageUrl = JsoupUtils.getAttr(page, "div#div_currpage a:contains(下一页)", "href").get(0);
        WebResponse response = null;
        while (StringUtils.isNotEmpty(nextPageUrl)) {
            WebRequest request = new WebRequest(nextPageUrl);
            response = client.execute(request);
            if (StringUtils.isNotEmpty(response.getRespText())) {
                contentBuffer.append(ExtractorUtils.extractorXinhuaContent(response.getRespText(), url));
                List<String> nextPageUrls = JsoupUtils.getAttr(response.getRespText(), "div#div_currpage a:contains(下一页)", "href");
                if (CollectionUtils.isEmpty(nextPageUrls)) {
                    nextPageUrl = null;
                } else {
                    nextPageUrl = nextPageUrls.get(0);
                }
            } else {
                nextPageUrl = null;
            }
        }
        news.setContent(contentBuffer.toString());
        news.setReferUrl(url);

        return news;
    }

}
