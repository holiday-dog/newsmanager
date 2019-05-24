package com.code.crawl;

import com.code.common.crawl.RequestMethod;
import com.code.common.crawl.WebClient;
import com.code.common.crawl.WebRequest;
import com.code.common.crawl.WebResponse;
import com.code.common.proxy.TrialProxyPlugin;
import com.code.common.proxyplugin.BiTeProxyPlugin;
import com.code.common.utils.UnicodeUtils;
import org.junit.Test;

import java.io.IOException;

public class ProxyTest {
    @Test
    public void test1() throws IOException, ClassNotFoundException {
        TrialProxyPlugin proxyPlugin = null;
        proxyPlugin = new BiTeProxyPlugin();
//        proxyPlugin = new JiGuangProxyPlugin();
//        proxyPlugin = new ZhiMaProxyPlugin();
//        proxyPlugin = new GxbProxyPlugin();

        System.out.println(proxyPlugin.getProxy());
    }

    @Test
    public void test4() {
//        System.out.println(BrowersUA.OPERA.randomUa());


        WebClient client = WebClient.buildDefaultClient().build();
//        System.out.println(client.getUser_Agent());
        WebResponse resp = null;
        try {
            resp = client.execute(new WebRequest("https://www.taobao.com/"));
            System.out.println(resp.getRespText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testClient() throws IOException {
        WebClient client = WebClient.buildDefaultClient().build();
//        WebRequest request = new WebRequest("http://pc.bitdaili.com/Users-login.html");
//        request.setMethod(RequestMethod.POST_STRING);
//        request.setRequestBodyString("username=holidaycat&password=m13354612723&type=web");
//        try {
//            WebResponse response = client.execute(request);
//            System.out.println(response.getRespText());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        WebRequest request1 = new WebRequest("http://pc.bitdaili.com/Index-getFree.html");
//        request1.setMethod(RequestMethod.POST_STRING);
//        request1.setRequestBodyString("");
//        request1.addRequestHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.157 Safari/537.36");
//        request1.setCookie("PHPSESSID=8ogo2kk8url0dnk76dquo627p1");
//        WebResponse response = client.execute(request1);
//        System.out.println(response.getRespText());
//
//        WebRequest request2 = new WebRequest("http://pc.bitdaili.com//Users-index.html");
////        request2.setMethod(RequestMethod.POST_STRING);
////        request2.setRequestBodyString("");
//        request2.addRequestHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.157 Safari/537.36");
//        WebResponse response2 = client.execute(request2);
//        System.out.println(response2.getRespText());

        WebRequest request3 = new WebRequest("https://www.taobao.com/");
        WebResponse response3 = client.execute(request3);

         request3 = new WebRequest("https://www.baidu.com/");
         response3 = client.execute(request3);

        request3 = new WebRequest("https://www.qq.com/");
        response3 = client.execute(request3);

        request3 = new WebRequest("https://www.meituan.com/");
        response3 = client.execute(request3);



    }

    @Test
    public void test00() throws IOException, ClassNotFoundException {
        String s = "\\u60a8\\u5df2\\u7ecf\\u83b7\\u53d6\\u4e86\\u8d60\\u9001\\uff0c24\\u5c0f\\u65f6\\u540e\\u624d\\u80fd\\u518d\\u6b21\\u83b7\\u5f97\\u8d60\\u9001";

        System.out.println(UnicodeUtils.unicodeToString(UnicodeUtils.stringToUnicode("您已经获取了赠送，24小时后才能再次获得赠送")));
        System.out.println(UnicodeUtils.unicodeToString("\\u60a8\\u5df2\\u7ecf\\u83b7\\u53d6\\u4e86\\u8d60\\u9001\\uff0c24\\u5c0f\\u65f6\\u540e\\u624d\\u80fd\\u518d\\u6b21\\u83b7\\u5f97\\u8d60\\u9001"));
        System.out.println(UnicodeUtils.unicodeToCn("\\u60a8\\u5df2\\u7ecf\\u83b7\\u53d6\\u4e86\\u8d60\\u9001\\uff0c24\\u5c0f\\u65f6\\u540e\\u624d\\u80fd\\u518d\\u6b21\\u83b7\\u5f97\\u8d60\\u9001"));
//        List<Class> plugins = ProxyUtils.getProxyPlugin();
//        for (Class cls : plugins) {
//            System.out.println(cls.getName());
//        }
//        Class c = YiZhouProxyPlugin.class;
//        for(Annotation a:c.getAnnotations()){
//            System.out.println(a.getClass().getName());
//        }
    }
}
