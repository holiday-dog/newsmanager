package com.code;

import com.code.common.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Testt {
    @Test
    public void testpage() throws IOException {
        InputStream inputStream = Testt.class.getClassLoader().getResourceAsStream("test/page2.html");
        String page = IOUtils.getStringByInputStream(inputStream);

//        for(String url: JsoupUtils.getAttr(page, "ul#gd_content li a", "href")){
//            System.out.println(url);
//        }

//        String ss = PatternUtils.groupOne(page, "jQuery\\d+_\\d+\\(([^\\(\\)]+)\\)", 1);
//        List<String> lists= JsonPathUtils.getValueList(ss, "$.data.list[*].LinkUrl");
//        System.out.println(JSON.toJSONString(lists));

        String title = JsoupUtils.getText(page, "div.h-title").trim();
        String keyword = JsoupUtils.getAttr(page, "meta[name='keywords']", "content").get(0);
        String description = JsoupUtils.getAttr(page, "meta[name='description']", "content").get(0);
        description = StringUtils.substringAfter(description, "---");
        String content = JsoupUtils.getElementsHtmlPage(page, "div#p-detail p");
        String pubTime = JsoupUtils.getText(page, "div.h-info span.h-time");
        String source = JsoupUtils.getText(page, "div.h-info em#source");
        String author = JsoupUtils.getText(page, "span:contains(责任编辑)");
        List<String> images = JsoupUtils.getAttr(content, "img", "src");
        if (!CollectionUtils.isEmpty(images)) {
            content = JsoupUtils.replaceAttrAppendValue(content, "img", "src", "http://");
        }
        System.out.println(keyword);
        System.out.println(title);
        System.out.println(description);
        System.out.println(content);
        System.out.println(pubTime);
        System.out.println(author);
        System.out.println(source);

    }

    @Test
    public void test1() {
        LocalDateTime dateTime = LocalDateTime.now();
        String ss = DateUtils.formatDateTime(dateTime, "yyyy-MM-dd");
        System.out.println(ss);

        long tt = DateUtils.timestampByDateTime(dateTime);
        System.out.println(tt);
        System.out.println(DateUtils.formatDateTime(DateUtils.dateTimeByTimeStamp(tt), "yyyy-MM-dd"));
//        System.out.println(DateUtils.form);
    }

    @Test
    public void test2() throws DocumentException {
        String s =
                "<users>\n" +
                        "    <user id=\"001\" name=\"eric\" password=\"123456\">ssss</user>\n" +
                        "    <user id=\"002\" name=\"rose\" password=\"123456\">aaa</user>\n" +
                        "    <user id=\"003\" name=\"jack\" password=\"123456\">vvvv</user>\n" +
                        "</users>\n";

        for (String sss : XpathUtils.evaluate(s, "//user[@id='001']/@name")) {
            System.out.println(sss);
        }
        for (String sss : XpathUtils.evaluateElementsText(s, "//user")) {
            System.out.println(sss);
        }
        System.out.println(DateUtils.nowTimeStamp());
    }

    @Test
    public void test5() throws IOException {
        InputStream inputStream = Testt.class.getClassLoader().getResourceAsStream("test/page.html");
        String page = IOUtils.getStringByInputStream(inputStream);

        String msg = JsoupUtils.getElementsHtmlPage(page, "div#p-detail p");

        String ss = JsoupUtils.replaceAttrAppendValue(msg, "img", "src", "http://");
        System.out.println(ss);
    }

    @Test
    public void test6() throws IOException {
        InputStream inputStream = Testt.class.getClassLoader().getResourceAsStream("test/page3.html");
        String page = IOUtils.getStringByInputStream(inputStream);

        List<Map<String, String>> pages = JsonPathUtils.getObjList(page, "$.data.list[*]");

       for(Map<String, String> map:pages){
           System.out.println(map.get("Title"));
       }
    }
}
