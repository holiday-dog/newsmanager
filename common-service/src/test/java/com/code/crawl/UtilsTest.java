package com.code.crawl;

import com.code.common.utils.EncryptUtils;
import com.code.common.utils.JsonPathUtils;
import com.code.common.utils.PatternUtils;
import org.apache.commons.codec.Encoder;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;
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
        s = "{\"code\":5,\"success\":\"false\",\"msg\":\"36.23.159.12登录IP不是白名单IP，请在用户中心添加该白名单\",\"data\":[]}";

        System.out.println(PatternUtils.groupOne(s, "(\\d+\\.\\d+\\.\\d+\\.\\d+)", 1));
//        System.out.println(PatternUtils.groupOne(s, "(\\d{1};;)", 1));
//        for (String ss : PatternUtils.groupAllIndex(s, "(\\d{1};;)", 1)) {
//            System.out.println(ss);
//        }
    }

    @Test
    public void ttest() throws Exception {
        String sss = "{\"state\":0,\"msg\":\"预检测通过\",\"data\":{\"ip\":\"36.18.108.21\",\"url\":\"https:\\/\\/too.ueuz.com\\/frontapi\\/public\\/http\\/get_ip\\/index?type=3174&iptimelong=1&ipcount=1&protocol=0&areatype=1&area=&resulttype=txt&duplicate=0&separator=1&other=&show_city=0&show_carrier=0&show_expire=0&isp=-1&auth_key=ea5adc375a93d1cdc8c83259d66f579b&app_key=3e13c9f643413f76802ff29d43b7ee22&timestamp=1558876899&sign=F1C49FF0E2006E64E69F4E384FB4CFDE\"}}";
        System.out.println(JsonPathUtils.getValue(sss, "$.data.url"));

        //auth_key=bcf3d856b2a991d477cdad20fa62277b&app_key=4000e58a6b8e728e1162a735e61ba2d7&timestamp=1558843394&sign=777A50F7834363A980BA07E210A682AB
        String ss = String.format("%s%s%s", "4000e58a6b8e728e1162a735e61ba2d7", "1558843394", "bcf3d856b2a991d477cdad20fa62277b");
        System.out.println(EncryptUtils.getMd5(ss));
//        System.out.println(EncryptUtils.getSha(ss));
        System.out.println(DigestUtils.md5Hex(ss));
//        System.out.println(DigestUtils.sha1Hex(ss));
//        System.out.println(DigestUtils.sha256Hex(ss));
        System.out.println(Base64.getEncoder().encodeToString("12345".getBytes()));
    }
}
