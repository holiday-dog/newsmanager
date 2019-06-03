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

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
}
