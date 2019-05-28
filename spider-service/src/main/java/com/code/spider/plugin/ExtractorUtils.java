package com.code.spider.plugin;

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

    public static String genHostPrex(String url) {
        return StringUtils.substringBeforeLast(url, "/");
    }
}
