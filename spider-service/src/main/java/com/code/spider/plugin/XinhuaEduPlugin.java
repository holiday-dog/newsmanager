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
    private static WebClient client = WebClient.buildDefaultClient().build();
    private Logger logger = LoggerFactory.getLogger(XinhuaEduPlugin.class);

    @Override
    public String getClientPluginName() {
        return "Xinhua_Edu_Plugin";
    }

    @Override
    Map<String, Object> preProcess(Map<String, Object> resultMap) {
        resultMap.put("spiderDate", LocalDateTime.now());
        resultMap.put("moduleType", Modules.EDUCATION);
        resultMap.put("spiderWebsite", "XinHua");

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
            List<RawData> newestEduList = new ArrayList<>();
            List<String> newestEduUrlList = JsoupUtils.getAttr(response.getRespText(), "ul.newestList li a", "href");
            if (!CollectionUtils.isEmpty(newestEduUrlList)) {
//                for (String newestEduUrl : newestEduUrlList) {
                for (int i = 0; i < 1; i++) {
                    String newestEduUrl = newestEduUrlList.get(i);
                    request = new WebRequest(newestEduUrl);
                    response = client.execute(request);
                    newestEduList.add(new RawData(newestEduUrl, response.getRespText()));
                }
            }
            spiderData.put("newestEduList", newestEduList);
        }

        //新闻历史
        List<RawData> eduPageList = new ArrayList<>();
        long ts = DateUtils.nowTimeStamp();
        for (int i = 1; i <= 1; i++) {
            String spiderUrl = String.format(eduListUrl, i, Constants.spiderPageNum, ts, ts);
            request = new WebRequest(spiderUrl);
            response = client.execute(request);

            String page = PatternUtils.groupOne(response.getRespText(), "jQuery\\d+_\\d+\\(([^\\(\\)]+)\\)", 1);
            List<String> linkUrls = JsonPathUtils.getValueList(page, "$.data.list[*].LinkUrl");
            for (int j = 0; j < 2; j++) {
                request = new WebRequest(linkUrls.get(j));
                response = client.execute(request);
                eduPageList.add(new RawData(linkUrls.get(j), response.getRespText()));
            }
        }
        spiderData.put("historyEduList", eduPageList);

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
                String title = JsoupUtils.getText(page, "div.h-title").trim();
                String keyword = JsoupUtils.getAttr(page, "meta[name='keywords']", "content").get(0);
                String description = JsoupUtils.getAttr(page, "meta[name='description']", "content").get(0);
                description = StringUtils.substringAfter(description, "---");
                String content = JsoupUtils.getElementsHtmlPage(page, "div#p-detail p");
                String pubTime = JsoupUtils.getText(page, "div.h-info span.h-time");
                String source = JsoupUtils.getText(page, "em#source");
                String author = JsoupUtils.getText(page, "span:contains(责任编辑)");
                author = StringUtils.substringAfter(author, "责任编辑：").trim();
                List<String> images = JsoupUtils.getAttr(content, "img", "src");
                if (!CollectionUtils.isEmpty(images)) {
                    content = JsoupUtils.replaceAttrAppendValue(content, "img", "src", genHostPrex(result.getUrl()) + "/");
                }

                News news = new News(title, description, keyword, author, content, DateUtils.parseDateTime(pubTime), source, result.getUrl());
                newsList.add(news);
            }
            resultMap.put(key, newsList);
        }
        System.out.println(JSON.toJSONString(resultMap));

        return resultMap;
    }
}
