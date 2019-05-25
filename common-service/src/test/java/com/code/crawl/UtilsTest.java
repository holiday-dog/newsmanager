package com.code.crawl;

import com.code.common.utils.EncryptUtils;
import com.code.common.utils.JsonPathUtils;
import com.code.common.utils.PatternUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class UtilsTest {
    @Test
    public void testt() {
        System.out.println(JsonPathUtils.getValue("{\"code\":200,\"msg\":\"ok\",\"data\":[{\"ip\":\"113.229.175.61\",\"port\":5412,\"expire_time\":\"2019-05-25 16:16:18\",\"city\":\"\\u978d\\u5c71\",\"isp\":\"\\u8054\\u901a\"}]}", "$.data[0].ip"));
        System.out.println(JsonPathUtils.getValue("{\"code\":200,\"msg\":\"ok\",\"data\":[{\"ip\":\"113.229.175.61\",\"port\":5412,\"expire_time\":\"2019-05-25 16:16:18\",\"city\":\"\\u978d\\u5c71\",\"isp\":\"\\u8054\\u901a\"}]}", "$.data[0].port"));
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
        for (String ss : PatternUtils.groupAllIndex(s, "(\\d{1};;)", 1)) {
            System.out.println(ss);
        }
    }

    @Test
    public void ttest() {
        String ss = String.format("%s%s%s", "c210e08361581ec1492ff087baf7e5ef", "3e13c9f643413f76802ff29d43b7ee22", "1558784290");
        System.out.println(EncryptUtils.getMd5(ss));
    }
}
