package com.code.spider.plugin;

import com.code.common.bean.HotNews;
import com.code.common.bean.News;
import com.code.common.crawl.WebClient;
import com.code.common.crawl.WebRequest;
import com.code.common.crawl.WebResponse;
import com.code.common.enums.Modules;
import com.code.common.enums.NewsType;
import com.code.common.utils.*;
import com.code.spider.bean.Constants;
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

public class XinhuaEduPlugin extends ClientPlugin {
    private static String indexUrl = "http://education.news.cn/";
    private static final String eduListUrl = "http://qc.wa.news.cn/nodeart/list?nid=11109063&pgnum=%s&cnt=%s&tp=1&orderby=1?callback=jQuery112409162368214565164_%s&_=%s";
    private static WebClient client = WebClient.buildDefaultClient().buildRouteAndCount(30, 50).build();
    private Logger logger = LoggerFactory.getLogger(XinhuaEduPlugin.class);

    @Override
    public String getClientPluginName() {
        return "XinhuaEduPlugin";
    }

    @Override
    Map<String, Object> preProcess(Map<String, Object> resultMap) {
        resultMap.put("spiderDate", DateUtils.formatDateTime(LocalDateTime.now()));
        resultMap.put("moduleType", Modules.EDUCATION.getValue());
        resultMap.put("spiderWebsite", "XinHua");
        resultMap.put("pluginName", getClientPluginName());

        return resultMap;
    }

    @Override
    Map<String, Object> process(Map<String, Object> resultMap) throws IOException {
        WebRequest request = null;
        WebResponse response = null;
        Map<String, Object> spiderData = new HashMap<>();
        //今日新闻
        request = new WebRequest("http://education.news.cn/");
        response = client.execute(request);

        if (StringUtils.isNotEmpty(response.getRespText())) {
            List<String> newestEduUrlList = JsoupUtils.getAttr(response.getRespText(), "ul.newestList li a", "href");
            List<String> hotEduList = JsoupUtils.getElementsHtml(response.getRespText(), "div[class$=foucos-container] div.swiper-wrapper div.swiper-slide:has(a)");
            if (!CollectionUtils.isEmpty(newestEduUrlList)) {
                List<RawData> newestEduList = new ArrayList<>();
                for (String newestEduUrl : newestEduUrlList) {
//                for (int i = 0; i < 2; i++) {
//                    String newestEduUrl = newestEduUrlList.get(i);
                    request = new WebRequest(newestEduUrl);
                    response = client.execute(request);
                    newestEduList.add(new RawData(newestEduUrl, response.getRespText(), NewsType.LATEST));
                }
                spiderData.put("lastestList", newestEduList);
            }
            //新闻热点
            if (!CollectionUtils.isEmpty(hotEduList)) {
                spiderData.put("hotList", hotEduList);
            }
        }
        //新闻历史
        long ts = DateUtils.nowTimeStamp();
        for (int i = 1; i <= 1; i++) {
            String spiderUrl = String.format(eduListUrl, i, Constants.spiderPageNum, ts, ts);
            request = new WebRequest(spiderUrl);
            response = client.execute(request);

            String page = PatternUtils.groupOne(response.getRespText(), "jQuery\\d+\\_\\d+\\((.*)", 1);
            List<String> linkUrls = JsonPathUtils.getValueList(page, "$.data.list[*].LinkUrl");
            if (!CollectionUtils.isEmpty(linkUrls)) {
                List<RawData> eduPageList = new ArrayList<>();
                for (int j = 0; j < 2; j++) {
                    request = new WebRequest(linkUrls.get(j));
                    response = client.execute(request);
                    eduPageList.add(new RawData(linkUrls.get(j), response.getRespText(), NewsType.HISTORY));
                }
                spiderData.put("historyList", eduPageList);
            }
        }

        return spiderData;
    }

    @Override
    Map<String, Object> handleData(Map<String, Object> spiderData) {
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
                    News news = ExtractorUtils.extractXinhua(page);
                    news.setContent(ExtractorUtils.extractorXinhuaContent(page, result.getUrl()));
                    news.setReferUrl(result.getUrl());
                    news.setNewsType(result.getNewsType());
                    news.setSign(RandomUtils.nextString());

                    newsList.add(news);
                }
                resultMap.put(key, newsList);
            } else {
                List<HotNews> hotNewsList = new ArrayList<>();
                List<String> hotNewPageList = (List<String>) spiderData.get(key);
                for (String hotNewsPage : hotNewPageList) {
                    hotNewsList.add(ExtractorUtils.extractXinhuaHot(hotNewsPage, indexUrl));
                }
                resultMap.put(key, hotNewsList);
            }
        }
//        int a = 1 / 0;

        return resultMap;
    }

    @Override
    public void retrySetClient(WebClient client) {
        client = client;
    }
}
