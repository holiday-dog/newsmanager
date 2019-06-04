package com.code.analyse.handler;

import com.code.common.bean.News;
import com.code.common.crawl.RequestMethod;
import com.code.common.crawl.WebClient;
import com.code.common.crawl.WebRequest;
import com.code.common.crawl.WebResponse;
import com.code.common.utils.DateUtils;
import com.code.common.utils.JsonPathUtils;
import com.code.common.utils.JsoupUtils;
import com.code.common.utils.PatternUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchExtractor {
    private static WebClient client = WebClient.buildDefaultClient().build();
    private static final String preImg = "http://tpic.home.news.cn/xhCloudNewsPic/";

    public List<News> searchKeyWord(String keyword) throws IOException {
        List<News> newsList = new ArrayList<>();
        searchXinhua(keyword, newsList);
        searchRenmin(keyword, newsList);

        return newsList;
    }

    public void searchXinhua(String keyWord, List<News> newsList) throws IOException {
        //新华网
        String searchUrl = "http://so.news.cn/getNews?keyword=" + URLEncoder.encode(keyWord) + "&curPage=1&sortField=0&searchFields=1&lang=cn";
        WebRequest request = new WebRequest(searchUrl);
        WebResponse response = client.execute(request);
        String page = response.getRespText();

        if (page != null && StringUtils.isNotEmpty(page)) {
            List<Map<String, Object>> maps = JsonPathUtils.getMapList(page, "$.content.results[*]");
            for (Map<String, Object> map : maps) {
                News news = new News();
                news.setTitle((String) map.get("title"));
                news.setImages(map.get("imgUrl") == null ? null : preImg + (String) map.get("imgUrl"));
                news.setDescription((String) map.get("des"));
                news.setKeywords((String) map.get("keyword"));
                news.setPubTime(DateUtils.parseDateTime((String) map.get("pubtime")));
                news.setReferUrl((String) map.get("url"));
                news.setSpiderWeb("Xinhua");

                newsList.add(news);
            }
        }
    }

    public void searchRenmin(String keyWord, List<News> newsList) throws IOException {
        //人民网
        String searchUrl = "http://search.people.com.cn/cnpeople/search.do";
        WebRequest request = new WebRequest(searchUrl);
        request.setMethod(RequestMethod.POST_STRING);
        request.setRequestBodyString("siteName=news&pageNum=1&keyword=" + URLEncoder.encode(keyWord, Charset.forName("gbk").toString()) + "&facetFlag=true&nodeType=belongsId&nodeId=0&pageCode=&originName=");
        WebResponse response = client.execute(request);
        String page = response.getRespText();

        if (page != null && StringUtils.isNotEmpty(page)) {
            List<String> searchList = JsoupUtils.getElementsHtml(page, "div[class~=w800] ul");
            for (String search : searchList) {
                News news = new News();
                news.setTitle(JsoupUtils.getText(search, "ul li:eq(0) a"));
                news.setReferUrl(JsoupUtils.getAttr(search, "ul li:eq(0) a", "href").get(0));
                String desc = JsoupUtils.cleanText(JsoupUtils.getElementsHtmlPage(search, "ul li:eq(1)"));
                news.setDescription(desc.replace("\n", ""));
                String pubTime = JsoupUtils.getText(search, "ul li:eq(2)");
                pubTime = PatternUtils.groupOne(pubTime, "(\\d{4}-?\\d{2}-?\\d{2}\\s\\d{2}:?\\d{2}:?\\d{2})", 1);
                news.setPubTime(DateUtils.parseDateTime(pubTime));
                news.setSpiderWeb("Renmin");

                newsList.add(news);
            }
        }
    }

    public List<News> relationKeyWord(String keyword) throws IOException {
        List<News> newsList = new ArrayList<>();
        searchRenmin(keyword, newsList);

        return newsList.stream().limit(10).collect(Collectors.toList());
    }


    public News searchContent(String referUrl, String spiderWeb) throws Exception {
        switch (spiderWeb) {
            case "Xinhua":
                return searchXinhuaContent(referUrl);
            case "Renmin":
                return searchRenminContent(referUrl);
        }
        return null;
    }

    public News searchXinhuaContent(String referUrl) {
        News news = new News();
        return news;
    }

    public News searchRenminContent(String referUrl) throws IOException {
        News news = new News();
        WebRequest request = new WebRequest(referUrl);
        WebResponse response = client.execute(request);
        String page = response.getRespText(Charset.forName("GB2312"));

        if (StringUtils.isNotEmpty(page)) {
            news = extractRenmin(page);
            news.setContent(extractRenminContent(page, referUrl));
        }

        return news;
    }


    public static News extractRenmin(String page) {
//        String title = JsoupUtils.getText(page, "#jtitle + h1").trim();
//        if (StringUtils.isEmpty(title)) {//pre
//            title = JsoupUtils.getText(page, "h3.pre + h1").trim();
//        }
        String title = JsoupUtils.getText(page, "title");
        String keyword = JsoupUtils.getAttr(page, "meta[name='keywords']", "content").get(0);
        String description = JsoupUtils.getAttr(page, "meta[name='description']", "content").get(0);
        String pubTime = JsoupUtils.getText(page, "p.sou");
        pubTime = PatternUtils.groupOne(pubTime, "(\\d{4}.\\d{2}.\\d{2}.{1,2}\\d{2}:\\d{2})", 1);
        LocalDateTime dateTime = null;
        if (StringUtils.isNotEmpty(pubTime)) {
            dateTime = DateUtils.parseDateTime(pubTime, "yyyy年MM月dd日HH:mm");
        }
        String source = JsoupUtils.getAttr(page, "meta[name='source']", "content").get(0);
        source = StringUtils.substringAfter(source, "来源：");
        String author = JsoupUtils.getText(page, "div[class~=text_title] p.author");
        if (StringUtils.isEmpty(author)) {
            author = JsoupUtils.getText(page, "div.edit");
            author = PatternUtils.groupOne(author, "\\(责编：?:?(.+)\\)", 1);
        }

        News news = new News(title, description, keyword, author, dateTime, source);
        return news;
    }

    public static String extractRenminContent(String page, String url) {
        String content = JsoupUtils.getElementsHtmlPage(page, "div#rwb_zw");
        if (StringUtils.isNotEmpty(content)) {
            content = JsoupUtils.removeElement(content, "div.edit");
            content = JsoupUtils.removeElement(content, "div.zdfy");
            content = JsoupUtils.removeElement(content, "center:has(table)");
            List<String> images = JsoupUtils.getAttr(content, "img", "src");
            if (!CollectionUtils.isEmpty(images)) {
                for (String image : images) {
                    if (!image.contains("http://")) {
                        content = JsoupUtils.replaceAttrAppendValue(content, "img[src=" + image + "]", "src", genPrex(url));
                    }
                }
            }
        } else {//text_img
            String pre = JsoupUtils.getElementsHtmlPage(page, "div[class~=text_img]");
            String pre2 = JsoupUtils.getElementsHtmlPage(page, "div[class~=show_text]");
            content = pre + pre2;

            if (StringUtils.isEmpty(content)) {
                content = JsoupUtils.getElementsHtmlPage(page, "div[class~=box_text]");
            }
        }

        return content;
    }

    public static String genPrex(String url) {
        return StringUtils.substring(url, 0, StringUtils.indexOf(url, "/", 8));
    }
}
