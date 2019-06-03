package com.code.spider.plugin;

import com.code.common.bean.HotNews;
import com.code.common.bean.News;
import com.code.common.crawl.WebClient;
import com.code.common.crawl.WebRequest;
import com.code.common.crawl.WebResponse;
import com.code.common.enums.Modules;
import com.code.common.enums.NewsType;
import com.code.common.utils.JsoupUtils;
import com.code.spider.bean.RawData;
import org.apache.commons.lang3.StringUtils;
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

public class RenminRecommendPlugin extends ClientPlugin {
    private static final String indexUrl = "http://www.people.com.cn/";
    private static WebClient client = WebClient.buildDefaultClient().buildRouteAndCount(30, 100).build();
    private static final Charset charset = Charset.forName("GB2312");
    private Logger logger = LoggerFactory.getLogger(XinhuaTravelPlugin.class);

    @Override
    public String getClientPluginName() {
        return "RenminRecommendPlugin";
    }

    @Override
    Map<String, Object> preProcess(Map<String, Object> resultMap) {
        resultMap.put("spiderDate", LocalDateTime.now());
        resultMap.put("moduleType", Modules.RECOMMEND);
        resultMap.put("spiderWebsite", "Renmin");
        resultMap.put("pluginName", getClientPluginName());

        return resultMap;
    }

    @Override
    Map<String, Object> process(Map<String, Object> resultMap) throws IOException {
        WebRequest request = null;
        WebResponse response = null;
        Map<String, Object> spiderData = new HashMap<>();
        request = new WebRequest(indexUrl);
        response = client.execute(request);

        if (StringUtils.isNotEmpty(response.getRespText(charset))) {
            //今日新闻
            List<String> especialUrlList = JsoupUtils.getAttr(response.getRespText(), "section#rmw_tuijian div.cont_b_box ul li h4 a", "href");
            List<String> topUrlList = JsoupUtils.getAttr(response.getRespText(), "div#rmw_a ul.list14 li a", "href");
            List<String> hotList = JsoupUtils.getElementsHtml(response.getRespText(), "div#focus_list ul li:has(a)");

            //特别推荐
            if (!CollectionUtils.isEmpty(especialUrlList)) {
                List<RawData> newestTravelList = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    String especialUrl = especialUrlList.get(i);
                    request = new WebRequest(especialUrl);
                    response = client.execute(request);
                    newestTravelList.add(new RawData(especialUrl, response.getRespText(charset), NewsType.RECOMMEND));
                }
                spiderData.put("recommendList", newestTravelList);
            }
//            排名新闻
            if (!CollectionUtils.isEmpty(topUrlList)) {
                List<RawData> topList = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    System.out.println(topUrlList.get(i));
                    request = new WebRequest(topUrlList.get(i));
                    response = client.execute(request);
                    topList.add(new RawData(topUrlList.get(i), response.getRespText(charset), NewsType.TOP));
                }
                spiderData.put("topList", topList);
            }

//            新闻热点
            if (!CollectionUtils.isEmpty(hotList)) {
                spiderData.put("hotList", hotList);
            }
        }
        return spiderData;
    }

    @Override
    Map<String, Object> handleData(Map<String, Object> spiderData) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
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
                    News news = ExtractorUtils.extractRenmin(page);
                    news.setContent(ExtractorUtils.extractRenminContent(page, result.getUrl()));
                    news.setReferUrl(result.getUrl());
                    newsList.add(news);
                }
                resultMap.put(key, newsList);
            } else {
                List<HotNews> hotNewsList = new ArrayList<>();
                List<String> pages = (List<String>) spiderData.get(key);
                for (String page : pages) {
                    hotNewsList.add(ExtractorUtils.extractRenminHot(page, indexUrl));
                }
                resultMap.put(key, hotNewsList);
            }
        }

        return resultMap;
    }

    @Override
    public void retrySetClient(WebClient client) {
        client = client;
    }
}

