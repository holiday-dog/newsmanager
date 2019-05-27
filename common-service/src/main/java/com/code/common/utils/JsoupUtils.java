package com.code.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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

    public static List<String> getElementsText(String page, String cssPath) {
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

}
