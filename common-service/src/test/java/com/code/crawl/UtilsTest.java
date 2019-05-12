package com.code.crawl;

import com.code.common.utils.JsonPathUtils;
import com.code.common.utils.PatternUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class UtilsTest {
    @Test
    public void testt(){
        System.out.println(JsonPathUtils.getValue("{\"proxyplugin\":\"124.152.185.57:4329\",\"provCode\":\"620000\",\"createTime\":1557676394000,\"cityCode\":\"621000\",\"timestamp\":1557677535015}","$.proxyplugin"));
    }
    public void test() throws IOException {
        String s = IOUtils.resourceToString("/page.txt", Charset.defaultCharset());
        System.out.println(s);
        System.out.println(PatternUtils.match(s, "\\\\\\\"msg\\\\\\\":\\\\\\\"ok\\\\\\\""));


        System.out.println(Pattern.compile("\\\"msg\\\":\\\"ok\\\"").matcher(s).find());
        System.out.println(Pattern.compile("\\\\\\\"msg\\\\\\\":\\\\\\\"ok\\\\\\\"").matcher(s).find());
    }
    @Test
    public void test2() throws IOException {
        String s = "s,1;;w,4;;234;r,1";

        System.out.println(PatternUtils.groupOne(s, "(\\d{1};;)", 1));
        for(String ss:PatternUtils.groupAllIndex(s, "(\\d{1};;)", 1)){
            System.out.println(ss);
        }
    }
}
