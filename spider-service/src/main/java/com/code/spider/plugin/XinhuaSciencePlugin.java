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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XinhuaSciencePlugin extends ClientPlugin {
    private static String indexUrl = "http://www.news.cn/tech";
    private static WebClient client = WebClient.buildDefaultClient().buildRouteAndCount(50, 100).build();
    private Logger logger = LoggerFactory.getLogger(XinhuaSciencePlugin.class);

    @Override
    public String getClientPluginName() {
        return "XinhuaSciencePlugin";
    }

    @Override
    Map<String, Object> preProcess(Map<String, Object> resultMap) {
        resultMap.put("spiderDate", LocalDateTime.now());
        resultMap.put("moduleType", Modules.SCIENCE);
        resultMap.put("spiderWebsite", "XinHua");
        resultMap.put("pluginName", getClientPluginName());

        return resultMap;
    }

    @Override
    Map<String, Object> process(Map<String, Object> resultMap) throws IOException {
        WebRequest request = null;
        WebResponse response = null;
        Map<String, Object> spiderData = new HashMap<>();
        request = new WebRequest("http://www.news.cn/tech/index.htm");
        response = client.execute(request);

        if (StringUtils.isNotEmpty(response.getRespText())) {

            List<String> newestScienceUrlList = JsoupUtils.getAttr(response.getRespText(), "ul.newestList li a", "href");
            List<String> historyScienceUrlList = JsoupUtils.getAttr(response.getRespText(), "ul#showData0 + div#hideData3 ul li  h3 a", "href");
            List<String> topScienceUrlList = JsoupUtils.getAttr(response.getRespText(), "div.impNews div.textList ul li a", "href");
            List<String> hotScienceList = JsoupUtils.getElementsHtml(response.getRespText(), "div[class$=foucos-container] div.swiper-wrapper div.swiper-slide:has(a)");

            //今日新闻
            if (!CollectionUtils.isEmpty(newestScienceUrlList)) {
                List<RawData> newestSciencelList = new ArrayList<>();
                for (int i = 0; i < 2; i++) {
                    String newestScienceUrl = newestScienceUrlList.get(i);
                    request = new WebRequest(newestScienceUrl);
                    response = client.execute(request);
                    newestSciencelList.add(new RawData(newestScienceUrl, response.getRespText(), NewsType.LATEST));
                }
                spiderData.put("lastestList", newestSciencelList);
            }

//            新闻历史
            if (!CollectionUtils.isEmpty(historyScienceUrlList)) {
                List<RawData> historyScienceList = new ArrayList<>();
                long ts = DateUtils.nowTimeStamp();
                for (int i = 0; i < 3; i++) {
                    request = new WebRequest(historyScienceUrlList.get(i));
                    response = client.execute(request);
                    historyScienceList.add(new RawData(historyScienceUrlList.get(i), response.getRespText(), NewsType.HISTORY));
                }
                spiderData.put("historyList", historyScienceList);
            }

            //新闻排名
            if (!CollectionUtils.isEmpty(topScienceUrlList)) {
                List<RawData> topSciencelList = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    String hotSciencelUrl = topScienceUrlList.get(i);
                    request = new WebRequest(hotSciencelUrl);
                    response = client.execute(request);
                    topSciencelList.add(new RawData(hotSciencelUrl, response.getRespText(), NewsType.TOP));
                }
                spiderData.put("topList", topSciencelList);
            }

//            新闻热点
            if (!CollectionUtils.isEmpty(hotScienceList)) {
                spiderData.put("hotList", hotScienceList);
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
                    if (page.contains("下一页") && page.contains("div_currpage")) {
                        newsList.add(handleMultiPage(page, result.getUrl()));
                    } else {
                        newsList.add(handleSinglePage(page, result.getUrl()));
                    }
                }
                resultMap.put(key, newsList);
            } else {
                List<HotNews> hotNewsListList = new ArrayList<>();
                List<String> pages = (List<String>) spiderData.get(key);
                for (String page : pages) {
                    hotNewsListList.add(ExtractorUtils.extractXinhuaHot(page, indexUrl));
                }
                resultMap.put(key, hotNewsListList);
            }
        }

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

    @Override
    public void retrySetClient(WebClient client) {
        client = client;
    }
}
