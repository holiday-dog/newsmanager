package com.code.spider.plugin;

import com.alibaba.fastjson.JSON;
import com.code.common.bean.HotNews;
import com.code.common.bean.News;
import com.code.common.crawl.WebClient;
import com.code.common.crawl.WebRequest;
import com.code.common.crawl.WebResponse;
import com.code.common.enums.Modules;
import com.code.common.enums.NewsType;
import com.code.common.utils.JsoupUtils;
import com.code.spider.bean.RawData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenminEduPlugin extends ClientPlugin {
    private static String indexUrl = "http://edu.people.com.cn/";
    private static WebClient client = WebClient.buildDefaultClient().buildRouteAndCount(50, 100).build();
    private static Charset charset = Charset.forName("GB2312");
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

        List<String> newestEduUrlList = JsoupUtils.getAttr(response.getRespText(), "div.jsnew_line a", "href");
        List<String> topEduUrlList = JsoupUtils.getAttr(response.getRespText(), "ul.ph_list li a", "href");
        List<String> historyEduUrlList = JsoupUtils.getAttr(response.getRespText(), "div.p1_content div.fr div.news_box ul li a", "href");
        List<String> hotEduList = JsoupUtils.getElementsHtml(response.getRespText(), "div#focus_list ul li");

        //即时新闻
        if (!CollectionUtils.isEmpty(newestEduUrlList)) {
            List<RawData> newestEduList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                String newestEduUrl = indexUrl + newestEduUrlList.get(i);
                System.out.println(newestEduUrl);
                request = new WebRequest(newestEduUrl);
                response = client.execute(request);
                newestEduList.add(new RawData(newestEduUrl, response.getRespText(charset), NewsType.LATEST));
            }
            spiderData.put("lastestList", newestEduList);
        }
        //新闻历史
        if (!CollectionUtils.isEmpty(historyEduUrlList)) {
            List<RawData> historyEduList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                String historyEduUrl = indexUrl + historyEduUrlList.get(i);
                System.out.println("------" + historyEduUrl);
                request = new WebRequest(historyEduUrl);
                response = client.execute(request);
                historyEduList.add(new RawData(historyEduUrl, response.getRespText(charset), NewsType.HISTORY));
            }
            spiderData.put("historyList", historyEduList);
        }
        //新闻排名
        if (!CollectionUtils.isEmpty(topEduUrlList)) {
            List<RawData> topEduList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                String hotEduUrl = indexUrl + topEduUrlList.get(i);
                request = new WebRequest(hotEduUrl);
                response = client.execute(request);
                topEduList.add(new RawData(hotEduUrl, response.getRespText(charset), NewsType.TOP));
            }
            spiderData.put("topList", topEduList);
        }
        //新闻热点
        if (!CollectionUtils.isEmpty(hotEduList)) {
            spiderData.put("hotList", hotEduList);
        }

        return spiderData;
    }

    @Override
    Map<String, Object> handleData(Map<String, Object> spiderData) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            for (String key : spiderData.keySet()) {
                logger.info("handler {} data", key);
                if (!key.contains("hot")) {
                    List<News> newsList = new ArrayList<>();
                    List<RawData> results = (List<RawData>) spiderData.get(key);
                    if (CollectionUtils.isEmpty(results)) {
                        continue;
                    }
                    for (RawData result : results) {
                        String page = result.getPage();
                        newsList.add(handleSinglePage(page, result.getUrl()));
                    }
                    resultMap.put(key, newsList);
                } else {
                    List<HotNews> hotNewsListList = new ArrayList<>();
                    List<String> pages = (List<String>) spiderData.get(key);
                    for (String page : pages) {
                        hotNewsListList.add(ExtractorUtils.extractRenminHot(page, indexUrl));
                    }
                    resultMap.put(key, hotNewsListList);
                }
            }
        } catch (Exception e) {
        }

        return resultMap;
    }

    private News handleSinglePage(String page, String url) {
        News news = ExtractorUtils.extractRenmin(page);
        news.setContent(ExtractorUtils.extractRenminContent(page, url));
        news.setReferUrl(url);
        return news;
    }

    public Map<String, Object> retryProcess(Map<String, Object> resultMap, WebClient client) throws IOException {
        client = client;
        return this.process(resultMap);
    }
}
