package com.code;

import com.code.common.utils.DateUtils;
import com.code.common.utils.IOUtils;
import com.code.common.utils.JsoupUtils;
import com.code.common.utils.XpathUtils;
import org.apache.commons.io.FileUtils;
import org.dom4j.DocumentException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;

public class Testt {
    @Test
    public void testpage() throws IOException {
        InputStream inputStream = Testt.class.getClassLoader().getResourceAsStream("test/page.html");
        String page = IOUtils.getStringByInputStream(inputStream);

        for(String url: JsoupUtils.getAttr(page, "ul#gd_content li a", "href")){
            System.out.println(url);
        }
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
}
