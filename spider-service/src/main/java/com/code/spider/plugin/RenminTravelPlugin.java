package com.code.spider.plugin;

import com.alibaba.fastjson.JSON;
import com.code.common.bean.News;
import com.code.common.crawl.WebClient;
import com.code.common.crawl.WebRequest;
import com.code.common.crawl.WebResponse;
import com.code.common.enums.Modules;
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

public class RenminTravelPlugin extends ClientPlugin {
    private static String indexUrl = "http://travel.people.com.cn";
    private static WebClient client = WebClient.buildDefaultClient().buildRouteAndCount(50, 100).build();
    private Logger logger = LoggerFactory.getLogger(RenminTravelPlugin.class);
    private static final Charset defaultCharset = Charset.forName("GB2312");

    @Override
    public String getClientPluginName() {
        return "Xinhua_Travel_Plugin";
    }

    @Override
    Map<String, Object> preProcess(Map<String, Object> resultMap) {
        resultMap.put("spiderDate", LocalDateTime.now());
        resultMap.put("moduleType", Modules.TRAVEL);
        resultMap.put("spiderWebsite", "XinHua");

        return resultMap;
    }

    @Override
    Map<String, Object> process(Map<String, Object> resultMap) throws IOException {
        WebRequest request = null;
        WebResponse response = null;
        Map<String, Object> spiderData = new HashMap<>();
        request = new WebRequest("http://travel.people.com.cn/");
        response = client.execute(request);

        //今日新闻
        List<String> newestTravelUrlList = JsoupUtils.getAttr(response.getRespText(), "div.jsnew_line a", "href");
        List<String> historyTravelUrlList = JsoupUtils.getAttr(response.getRespText(), "div.headingNews div.hdNews div.on h5 a", "href");
        List<String> topTravelUrlList = JsoupUtils.getAttr(response.getRespText(), "ul.ph_list li a", "href");
        if (!CollectionUtils.isEmpty(newestTravelUrlList)) {
            List<RawData> newestTravelList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                String newestTravelUrl = indexUrl + newestTravelUrlList.get(i);
                System.out.println("-----" + newestTravelUrl);
                request = new WebRequest(newestTravelUrl);
                response = client.execute(request);
                newestTravelList.add(new RawData(newestTravelUrl, response.getRespText(defaultCharset)));
            }
            spiderData.put("newestTravelList", newestTravelList);
        }
        //新闻历史
        if (!CollectionUtils.isEmpty(historyTravelUrlList)) {
            List<RawData> historyTravelList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                String historyTravelUrl = indexUrl + historyTravelUrlList.get(i);
                System.out.println("--" + historyTravelUrl);
                request = new WebRequest(historyTravelUrl);
                response = client.execute(request);
                historyTravelList.add(new RawData(historyTravelUrl, response.getRespText(defaultCharset)));
            }
            spiderData.put("historyTravelList", historyTravelList);
        }
        //排行新闻
        if (!CollectionUtils.isEmpty(topTravelUrlList)) {
            List<RawData> topTravelList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                String topTravelUrl = indexUrl + topTravelUrlList.get(i);
                System.out.println(topTravelUrl);
                request = new WebRequest(topTravelUrl);
                response = client.execute(request);
                topTravelList.add(new RawData(topTravelUrl, response.getRespText(defaultCharset)));
            }
            spiderData.put("topTravelList", topTravelList);
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
                    if (page.contains("next_page.jpg")) {
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

    public News handleSinglePage(String page, String url) {
        News news = ExtractorUtils.extractRenmin(page);
        news.setContent(ExtractorUtils.extractRenminContent(page, url));
        news.setReferUrl(url);
        return news;
    }

    public News handleMultiPage(String page, String url) throws IOException {
        News news = ExtractorUtils.extractRenmin(page);
        String content = ExtractorUtils.extractRenminContent(page, url);

        StringBuffer contentBuffer = new StringBuffer(content);
        String nextPageUrl = JsoupUtils.getAttr(page, "table:has(img[src~=next_page]) td:has(img[src~=next_page]) a", "href").get(0);
        WebResponse response = null;
        while (StringUtils.isNotEmpty(nextPageUrl)) {
            WebRequest request = new WebRequest(indexUrl + nextPageUrl);
            response = client.execute(request);
            page = response.getRespText(defaultCharset);
            if (StringUtils.isNotEmpty(page)) {
                content = ExtractorUtils.extractRenminContent(page, url);
                contentBuffer.append(JsoupUtils.removeElement(content, "div.otitle"));
                List<String> nextPageUrls = JsoupUtils.getAttr(page, "table:has(img[src~=next_page]) td:has(img[src~=next_page]) a", "href");
                if (!CollectionUtils.isEmpty(nextPageUrls)) {
                    nextPageUrl = nextPageUrls.get(0);
                } else {
                    break;
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
