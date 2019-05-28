package com.code.spider.plugin;

import com.alibaba.fastjson.JSON;
import com.code.common.bean.News;
import com.code.common.crawl.WebClient;
import com.code.common.crawl.WebRequest;
import com.code.common.crawl.WebResponse;
import com.code.common.enums.Modules;
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

public class XinhuaTravelPlugin extends ClientPlugin {
    private static String indexUrl = "http://education.news.cn/";
    private static final String preHotTravelUrl = "http://bd.xinhuanet.com/js/StdID.do?bfdid=1&appkey=%s";
    private static final String hotTravelUrl = "http://bd.xinhuanet.com/xhw/2.0/%s.do?unq=1&p_bid=%s&bidlst=%s&req=%s&tma=&tmc=&tmd=&pageflag=%s&fingerprint=%s&fpduration=0&sid=&cid=Cxhw&uid=123456&appkey=%s&d_s=pc&p_t=chl&gid=%s&callback=BCore.instances[2].callbacks[0]&random=%s";
    private static final String appKey = "0333404b77b18137ae80579b763fcfe7";
    private static final String hotUrlId = "A1510559300955";
    private static WebClient client = WebClient.buildDefaultClient().build();
    private Logger logger = LoggerFactory.getLogger(XinhuaTravelPlugin.class);

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
        request = new WebRequest("http://www.xinhuanet.com/travel/");
        response = client.execute(request);

        if (StringUtils.isNotEmpty(response.getRespText())) {
            //今日新闻
            List<String> newestTravelUrlList = JsoupUtils.getAttr(response.getRespText(), "ul.newestList li a", "href");
            List<String> historyTravelUrlList = JsoupUtils.getAttr(response.getRespText(), "div#hideData0 ul.dataList li h3 a", "href");

            if (!CollectionUtils.isEmpty(newestTravelUrlList)) {
                List<RawData> newestTravelList = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    String newestEduUrl = newestTravelUrlList.get(i);
                    System.out.println(newestEduUrl);
//                    request = new WebRequest(newestEduUrl);
//                    response = client.execute(request);
//                    newestTravelList.add(new RawData(newestEduUrl, response.getRespText()));
                }
                spiderData.put("newestTravelList", newestTravelList);
            }

            //新闻历史
            if (!CollectionUtils.isEmpty(newestTravelUrlList)) {
                List<RawData> historyTravelList = new ArrayList<>();
                long ts = DateUtils.nowTimeStamp();
                for (int i = 0; i <= 10; i++) {
                    System.out.println(historyTravelUrlList.get(i));
//                request = new WebRequest(historyTravelUrlList.get(i));
//                response = client.execute(request);
//                historyTravelList.add(new RawData(historyTravelUrlList.get(i), response.getRespText()));
                }
                spiderData.put("historyTravelList", historyTravelList);
            }
        }


        //热点新闻
//        request = new WebRequest(String.format(preHotTravelUrl, appKey));
//        response = client.execute(request);
//        String gid = PatternUtils.groupOne(response.getRespText(), "gid=\"(\\w+)\"", 1);
//
//        long ts = DateUtils.nowTimeStamp();
//        String url = String.format(hotTravelUrl, hotUrlId, hotUrlId, hotUrlId, hotUrlId, ts, RandomUtils.nextString(), appKey, gid, ts);
//        request = new WebRequest(url);
//        response = client.execute(request);

//        List<String> hotTravelUrlList = JsonPathUtils.getValueList(PatternUtils.groupOne(response.getRespText(), "callbacks\\[\\d+\\]\\(([^\\(\\)]+)\\)", 1), "$..[*].url");
//        if (!CollectionUtils.isEmpty(hotTravelUrlList)) {
//            List<RawData> hotTravelList = new ArrayList<>();
//            for (int i = 0; i < 5; i++) {
//                String hotTravelUrl = hotTravelUrlList.get(i);
////                System.out.println(hotTravelUrl);
//                request = new WebRequest(hotTravelUrl);
//                response = client.execute(request);
//                hotTravelList.add(new RawData(hotTravelUrl, response.getRespText()));
//            }
//        }

        return spiderData;
    }

    @Override
    Map<String, Object> handleData(Map<String, Object> spiderData) {
        Map<String, Object> resultMap = new HashMap<>();
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
//                    newsList.add();
                } else {
                    newsList.add(handleSinglePage(page, result.getUrl()));
                }
            }
            resultMap.put(key, newsList);
        }
        System.out.println(JSON.toJSONString(resultMap));

        return resultMap;
    }

    private News handleSinglePage(String page, String url) {
        String title = JsoupUtils.getText(page, "div.h-title").trim();
        String keyword = JsoupUtils.getAttr(page, "meta[name='keywords']", "content").get(0);
        String description = JsoupUtils.getAttr(page, "meta[name='description']", "content").get(0);
        description = StringUtils.substringAfter(description, "---");
        String content = JsoupUtils.getElementsHtmlPage(page, "div#p-detail p");
        String pubTime = JsoupUtils.getText(page, "div.h-info span.h-time");
        String source = JsoupUtils.getText(page, "div.h-info em#source");
        String author = JsoupUtils.getText(page, "span:contains(责任编辑)");
        author = StringUtils.substringAfter(author, "责任编辑：").trim();
        List<String> images = JsoupUtils.getAttr(content, "img", "src");
        if (!CollectionUtils.isEmpty(images)) {
            content = JsoupUtils.replaceAttrAppendValue(content, "img", "src", genHostPrex(url) + "/");
        }

        News news = new News(title, description, keyword, author, content, DateUtils.parseDateTime(pubTime), source, url);
        return news;
    }

    private News handleMultiPage(String page, String url) throws IOException {
        String title = JsoupUtils.getText(page, "div.h-title").trim();
        String keyword = JsoupUtils.getAttr(page, "meta[name='keywords']", "content").get(0);
        String description = JsoupUtils.getAttr(page, "meta[name='description']", "content").get(0);
        description = StringUtils.substringAfter(description, "---");
        String content = JsoupUtils.getElementsHtmlPage(page, "div#p-detail p");
        String pubTime = JsoupUtils.getText(page, "div.h-info span.h-time");
        String source = JsoupUtils.getText(page, "div.h-info em#source");
        String author = JsoupUtils.getText(page, "span:contains(责任编辑)");
        author = StringUtils.substringAfter(author, "责任编辑：").trim();
        List<String> images = JsoupUtils.getAttr(content, "img", "src");
        if (!CollectionUtils.isEmpty(images)) {
            content = JsoupUtils.replaceAttrAppendValue(content, "img", "src", genHostPrex(url) + "/");
        }

        StringBuffer contentBuffer = new StringBuffer(content);
        String nextPageUrl = JsoupUtils.getAttr(page, "div#div_currpage a:contains(下一页)", "href").get(0);
        WebResponse response = null;
        while (StringUtils.isEmpty(nextPageUrl)) {
            WebRequest request = new WebRequest(nextPageUrl);
            response = client.execute(request);
            if (StringUtils.isNotEmpty(response.getRespText())) {
                contentBuffer.append(JsoupUtils.getElementsHtmlPage(response.getRespText(), "div#p-detail p"));
                nextPageUrl = JsoupUtils.getAttr(response.getRespText(), "div#div_currpage a:contains(下一页)", "href").get(0);
            } else {
                nextPageUrl = null;
            }
        }


        News news = new News(title, description, keyword, author, content, DateUtils.parseDateTime(pubTime), source, url);
        return news;
    }
}
