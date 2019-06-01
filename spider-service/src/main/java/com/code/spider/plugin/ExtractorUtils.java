package com.code.spider.plugin;

import com.code.common.bean.HotNews;
import com.code.common.bean.News;
import com.code.common.utils.DateUtils;
import com.code.common.utils.JsoupUtils;
import com.code.common.utils.PatternUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

public class ExtractorUtils {
    public static News extractXinhua(String page) {
        String title = JsoupUtils.getText(page, "div.h-title").trim();
        if (StringUtils.isEmpty(title)) {
            title = JsoupUtils.getText(page, "span#title").trim();
        }
        String keyword = JsoupUtils.getAttr(page, "meta[name='keywords']", "content").get(0).trim();
        String description = JsoupUtils.getAttr(page, "meta[name='description']", "content").get(0).trim();
        description = StringUtils.substringAfter(description, "---");
        LocalDateTime dateTime = null;
        String pubTime = JsoupUtils.getText(page, "div.h-info span.h-time");
        if (StringUtils.isEmpty(pubTime)) {
            pubTime = JsoupUtils.getText(page, "span#pubtime");
        }
        if (pubTime.contains("年") && pubTime.contains("月")) {
            dateTime = DateUtils.parseDateTime(pubTime.trim(), "yyyy年MM月dd日 HH:mm:ss");
        } else {
            dateTime = DateUtils.parseDateTime(pubTime.trim());
        }
        String source = JsoupUtils.getText(page, "em#source");
        String author = JsoupUtils.getText(page, "span:contains(责任编辑)");
        if (StringUtils.isNotEmpty(author)) {
            author = PatternUtils.groupOne(author, "责任编辑:?：?([^\\<\\]]+)", 1).trim();
        }

        News news = new News(title, description, keyword, author, dateTime, source);
        return news;
    }

    public static HotNews extractXinhuaHot(String page, String hostPrex) {
        String title = JsoupUtils.getText(page, "p.name");
        String referUrl = JsoupUtils.getAttr(page, "a:eq(0)", "href").get(0);
        String img = hostPrex + "/" + JsoupUtils.getAttr(page, "a img", "src").get(0);
        HotNews hotNews = new HotNews(title, img, referUrl);
        return hotNews;
    }

    public static String extractorXinhuaContent(String page, String url) {
        String content = JsoupUtils.getElementsHtmlPage(page, "div#p-detail p");
        if (StringUtils.isEmpty(content)) {
            content = JsoupUtils.getElementsHtmlPage(page, "span#content p");
        }
        List<String> images = JsoupUtils.getAttr(content, "img", "src");
        if (!CollectionUtils.isEmpty(images)) {
            for (String image : images) {
                if (!image.contains("http://")) {
                    content = JsoupUtils.replaceAttrAppendValue(content, "img[src=" + image + "]", "src", genHostPrex(url) + "/");
                }
            }
        }
        return content;
    }

    public static News extractRenmin(String page) {
        String title = JsoupUtils.getText(page, "div[class~=text_title] h1").trim();
        String keyword = JsoupUtils.getAttr(page, "meta[name='keywords']", "content").get(0);
        String description = JsoupUtils.getAttr(page, "meta[name='description']", "content").get(0);
        String pubTime = JsoupUtils.getText(page, "div[class~=text_title] div.fl");
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

    public static HotNews extractRenminHot(String page, String hostPrex) {
        String title = JsoupUtils.getText(page, "div.show a");
        String img = hostPrex + JsoupUtils.getAttr(page, "a img", "src").get(0);
        String referUrl = JsoupUtils.getAttr(page, "a", "href").get(0);
        HotNews hotNews = new HotNews(title, img, referUrl);
        return hotNews;
    }

    public static String extractRenminContent(String page, String url) {
        String content = JsoupUtils.getElementsHtmlPage(page, "div#rwb_zw");
        if (StringUtils.isNotEmpty(content)) {
            content = JsoupUtils.removeElement(content, "div.edit");
            content = JsoupUtils.removeElement(content, "div.zdfy");
            content = JsoupUtils.removeElement(content, "center:has(table)");
//            content = JsoupUtils.removeElement(content, "table:has(img[src~=next_page])");
//            content = JsoupUtils.removeElement(content, "table:has(img[src~=prev_page])");
        }

        return content;
    }

    public static String genHostPrex(String url) {
        return StringUtils.substringBeforeLast(url, "/");
    }

    public static String genPrex(String url) {
        return StringUtils.substring(url, StringUtils.indexOf(url, "/", StringUtils.indexOf(url, "://")));
    }
}
