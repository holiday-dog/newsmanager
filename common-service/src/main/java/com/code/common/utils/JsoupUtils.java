package com.code.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class JsoupUtils {
    public static Document parse(String page) {
        Document document = Jsoup.parse(page);
        return document;
    }

    public static Elements getElements(String page, String cssPath) {
        if (StringUtils.isEmpty(page)) {
            return null;
        }
        Document document = parse(page);
        Elements elements = document.select(cssPath);
        return elements;
    }

    public static List<String> getAttr(String page, String cssPath, String attrName) {
        Elements elements = getElements(page, cssPath);
        if (elements == null) {
            return null;
        }
        List<String> attrValues = new ArrayList<>();
        for (Element element : elements) {
            attrValues.add(element.attr(attrName));
        }
        return attrValues;
    }

    public static List<String> getElementsHtml(String page, String cssPath) {
        Elements elements = getElements(page, cssPath);
        if (elements == null) {
            return null;
        }
        List<String> outHtmls = new ArrayList<>();
        for (Element element : elements) {
            outHtmls.add(element.outerHtml());
        }
        return outHtmls;
    }

    public static String getElementsHtmlPage(String page, String cssPath) {
        Elements elements = getElements(page, cssPath);
        if (elements == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (Element element : elements) {
            stringBuffer.append(element.outerHtml());
        }
        return stringBuffer.toString();
    }

    public static List<String> getTexts(String page, String cssPath) {
        Elements elements = getElements(page, cssPath);
        if (elements == null) {
            return null;
        }
        List<String> texts = new ArrayList<>();
        for (Element element : elements) {
            texts.add(element.text());
        }
        return texts;
    }

    public static String getText(String page, String cssPath) {
        Elements elements = getElements(page, cssPath);
        if (elements == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (Element element : elements) {
            stringBuffer.append(element.text());
        }
        return stringBuffer.toString();
    }

    public static String getText(String page, String cssPath, String separator) {
        Elements elements = getElements(page, cssPath);
        if (elements == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (Element element : elements) {
            stringBuffer.append(element.text());
            stringBuffer.append(separator);
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        return stringBuffer.toString();
    }

    public static String replaceAttr(String page, String cssPath, String key, String value) {
        Document document = Jsoup.parse(page);
        Elements elements = document.select(cssPath);
        if (elements == null) {
            return null;
        }
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            element.attr(key, value);
        }
        return document.body().html();
    }

    public static String replaceAttrAppendValue(String page, String cssPath, String key, String appendValue) {
        Document document = Jsoup.parse(page);
        Elements elements = document.select(cssPath);
        if (elements == null) {
            return null;
        }
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            element.attr(key, appendValue + element.attr(key));
        }
        return document.body().html();
    }

    public static String removeElement(String page, String cssPath) {
        Document document = Jsoup.parse(page);
        Elements elements = document.select(cssPath);
        if (elements == null || elements.size() == 0) {
            return document.body().html();
        }
        for (int i = 0; i < elements.size(); i++) {
            elements.get(i).remove();
        }

        return document.body().html();
    }

    public static String cleanText(String html) {
        String content = Jsoup.clean(html, new Whitelist().addTags("span", "font", "p").addAttributes("font", "color"));
        return content;
    }

}
