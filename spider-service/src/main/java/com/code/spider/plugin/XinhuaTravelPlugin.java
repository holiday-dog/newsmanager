package com.code.spider.plugin;

import com.code.common.bean.HotNews;
import com.code.common.bean.News;
import com.code.common.crawl.WebClient;
import com.code.common.crawl.WebRequest;
import com.code.common.crawl.WebResponse;
import com.code.common.enums.Modules;
import com.code.common.enums.NewsType;
import com.code.common.utils.*;
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

public class XinhuaTravelPlugin extends ClientPlugin {
    private static String indexUrl = "http://www.xinhuanet.com/travel/";
    private static final String preHotTravelUrl = "http://bd.xinhuanet.com/js/StdID.do?bfdid=1&appkey=%s";
    private static final String hotTravelUrl = "http://bd.xinhuanet.com/xhw/2.0/%s.do?unq=1&p_bid=%s&bidlst=%s&req=%s&tma=&tmc=&tmd=&pageflag=%s&fingerprint=%s&fpduration=0&sid=&cid=Cxhw&uid=123456&appkey=%s&d_s=pc&p_t=chl&gid=%s&callback=BCore.instances[2].callbacks[0]&random=%s";
    private static final String appKey = "0333404b77b18137ae80579b763fcfe7";
    private static final String hotUrlId = "A1510559300955";
    private static WebClient client = WebClient.buildDefaultClient().buildRouteAndCount(50, 100).build();
    private Logger logger = LoggerFactory.getLogger(XinhuaTravelPlugin.class);

    @Override
    public String getClientPluginName() {
        return "XinhuaTravelPlugin";
    }

    @Override
    Map<String, Object> preProcess(Map<String, Object> resultMap) {
        resultMap.put("spiderDate", DateUtils.formatDateTime(LocalDateTime.now()));
        resultMap.put("moduleType", Modules.TRAVEL.getValue());
        resultMap.put("spiderWebsite", "XinHua");
        resultMap.put("pluginName", getClientPluginName());

        return resultMap;
    }

    @Override
    Map<String, Object> process(Map<String, Object> resultMap) throws IOException {
        WebRequest request = null;
        WebResponse response = null;
        Map<String, Object> spiderData = new HashMap<>();
        request = new WebRequest("http://www.xinhuanet.com/travel/");
        response = client.execute(request);

        if (StringUtils.isNotEmpty(response.getRespText())) {
            //今日新闻
            List<String> newestTravelUrlList = JsoupUtils.getAttr(response.getRespText(), "ul.newestList li a", "href");
            List<String> historyTravelUrlList = JsoupUtils.getAttr(response.getRespText(), "div#hideData0 ul.dataList li h3 a", "href");
            List<String> hotEduList = JsoupUtils.getElementsHtml(response.getRespText(), "div[class$=foucos-container] div.swiper-wrapper div.swiper-slide:has(a)");
            List<String> hotTravelList = JsoupUtils.getElementsHtml(response.getRespText(), "div[class$=foucos-container] div.swiper-wrapper div.swiper-slide:has(a)");

            if (!CollectionUtils.isEmpty(newestTravelUrlList)) {
                //最新
                List<RawData> newestTravelList = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    String newestEduUrl = newestTravelUrlList.get(i);
//                    System.out.println("-----" + newestEduUrl);
                    request = new WebRequest(newestEduUrl);
                    response = client.execute(request);
                    newestTravelList.add(new RawData(newestEduUrl, response.getRespText(), NewsType.LATEST));
                }
                spiderData.put("lastestList", newestTravelList);
            }

            //新闻历史
            if (!CollectionUtils.isEmpty(historyTravelUrlList)) {
                List<RawData> historyTravelList = new ArrayList<>();
                long ts = DateUtils.nowTimeStamp();
                for (int i = 0; i < 3; i++) {
                    System.out.println(historyTravelUrlList.get(i));
                    request = new WebRequest(historyTravelUrlList.get(i));
                    response = client.execute(request);
                    historyTravelList.add(new RawData(historyTravelUrlList.get(i), response.getRespText(), NewsType.HISTORY));
                }
                spiderData.put("historyList", historyTravelList);
            }

            //新闻热点
            if (!CollectionUtils.isEmpty(hotTravelList)) {
                spiderData.put("hotList", hotTravelList);
            }
        }

        //热点新闻
        request = new WebRequest(String.format(preHotTravelUrl, appKey));
        response = client.execute(request);
        String gid = PatternUtils.groupOne(response.getRespText(), "gid=\"(\\w+)\"", 1);

        long ts = DateUtils.nowTimeStamp();
        String url = String.format(hotTravelUrl, hotUrlId, hotUrlId, hotUrlId, hotUrlId, ts, RandomUtils.nextString(), appKey, gid, ts);
        request = new WebRequest(url);
        response = client.execute(request);

        List<String> hotTravelUrlList = JsonPathUtils.getValueList(PatternUtils.groupOne(response.getRespText(), "callbacks\\[\\d+\\]\\(([^\\(\\)]+)\\)", 1), "$..[*].url");
        if (!CollectionUtils.isEmpty(hotTravelUrlList)) {
            List<RawData> topTravelList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                String hotTravelUrl = hotTravelUrlList.get(i);
//                System.out.println(hotTravelUrl);
                request = new WebRequest(hotTravelUrl);
                response = client.execute(request);
                topTravelList.add(new RawData(hotTravelUrl, response.getRespText(), NewsType.TOP));
            }
            spiderData.put("topList", topTravelList);
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
