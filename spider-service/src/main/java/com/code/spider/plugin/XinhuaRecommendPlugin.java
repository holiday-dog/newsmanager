package com.code.spider.plugin;

import com.code.common.bean.HotNews;
import com.code.common.bean.News;
import com.code.common.crawl.WebClient;
import com.code.common.crawl.WebRequest;
import com.code.common.crawl.WebResponse;
import com.code.common.enums.Modules;
import com.code.common.enums.NewsType;
import com.code.common.utils.DateUtils;
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

public class XinhuaRecommendPlugin extends ClientPlugin {
    private static final String indexUrl = "http://www.xinhuanet.com/";
    private static WebClient client = WebClient.buildDefaultClient().buildRouteAndCount(30, 100).build();
    private static final Charset charset = Charset.forName("GB2312");
    private Logger logger = LoggerFactory.getLogger(XinhuaTravelPlugin.class);

    @Override
    public String getClientPluginName() {
        return "XinhuaRecommendPlugin";
    }

    @Override
    Map<String, Object> preProcess(Map<String, Object> resultMap) {
        resultMap.put("spiderDate", DateUtils.formatDateTime(LocalDateTime.now()));
        resultMap.put("moduleType", Modules.RECOMMEND.getValue());
        resultMap.put("spiderWebsite", "Xinhua");
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

        if (StringUtils.isNotEmpty(response.getRespText())) {
            //今日新闻
            String page = response.getRespText();
            List<String> especialUrlList = JsoupUtils.getAttr(page, "ul#gd_content li a", "href");
            List<String> topUrlList = JsoupUtils.getAttr(page, "div#hpart2L a", "href");
            List<String> hotList = JsoupUtils.getElementsHtml(page, "div#focusBoxBody div.txt ul li");
            List<String> hotImgsList = JsoupUtils.getElementsHtml(page, "div#focusBoxBody div.focusBoxWrap ul li img");

            //特别推荐
            if (!CollectionUtils.isEmpty(especialUrlList)) {
                List<RawData> newestTravelList = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    String especialUrl = especialUrlList.get(i);
                    request = new WebRequest(especialUrl);
                    response = client.execute(request);
                    newestTravelList.add(new RawData(especialUrl, response.getRespText(), NewsType.RECOMMEND));
                }
                spiderData.put("recommendList", newestTravelList);
            }
            //排名新闻
            if (!CollectionUtils.isEmpty(topUrlList)) {
                List<RawData> topList = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    System.out.println(topUrlList.get(i));
                    request = new WebRequest(topUrlList.get(i));
                    response = client.execute(request);
                    topList.add(new RawData(topUrlList.get(i), response.getRespText(), NewsType.TOP));
                }
                spiderData.put("topList", topList);
            }

//            新闻热点
            if (!CollectionUtils.isEmpty(hotList)) {
                List<HotNews> hotNewsList = new ArrayList<>();
                for (int i = 0; i < hotList.size(); i++) {
                    HotNews hotNews = new HotNews();
                    String img = JsoupUtils.getAttr(hotImgsList.get(i), "img", "data-original").get(0);
                    if (StringUtils.isEmpty(img)) {
                        img = indexUrl + JsoupUtils.getAttr(hotImgsList.get(i), "img", "src").get(0);
                    }
                    hotNews.setImage(img);
                    hotNews.setReferUrl(JsoupUtils.getAttr(hotList.get(i), "a", "href").get(0));
                    hotNews.setTitle(JsoupUtils.getText(hotList.get(i), "a"));
                    hotNewsList.add(hotNews);
                }
                spiderData.put("hotList", hotNewsList);
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
                    News news = ExtractorUtils.extractXinhua(page);
                    news.setContent(ExtractorUtils.extractorXinhuaContent(page, result.getUrl()));
                    news.setReferUrl(result.getUrl());
                    news.setNewsType(result.getNewsType());
                    newsList.add(news);
                }
                resultMap.put(key, newsList);
            } else {
                resultMap.put(key, spiderData.get(key));
            }
        }

        return resultMap;
    }

    @Override
    public void retrySetClient(WebClient client) {
        client = client;
    }
}
