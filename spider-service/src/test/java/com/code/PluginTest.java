package com.code;

import com.code.common.crawl.WebClient;
import com.code.common.crawl.WebRequest;
import com.code.common.crawl.WebResponse;
import com.code.spider.executors.MainExecutor;
import com.code.spider.plugin.ClientPlugin;
import com.code.spider.plugin.RenminTravelPlugin;
import com.code.spider.plugin.XinhuaEduPlugin;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public class PluginTest {
    @Test
    public void testt() {
        ClientPlugin plugin = null;
        plugin = new XinhuaEduPlugin();
//        plugin = new XinhuaTravelPlugin();
//        plugin = new XinhuaSciencePlugin();
//        plugin = new RenminEduPlugin();
//        plugin = new RenminTravelPlugin();
//        plugin = new RenminRecommendPlugin();
//        plugin = new XinhuaRecommendPlugin();
//        System.out.println(plugin.genHostPrex("http://education.news.cn/2019-05/28/c_1210145273.htm"));
        System.out.println(plugin.spiderProcess(null));
    }

    @Test
    public void test1() throws IOException {
        ClientPlugin plugin = new RenminTravelPlugin();

        WebRequest req = new WebRequest("http://politics.people.com.cn/n1/2019/0601/c1001-31114974.html");
        WebResponse resp = WebClient.buildDefaultClient().build().execute(req);
        System.out.println(resp.getRespText(Charset.forName("GB2312")));
//        News news = ((RenminTravelPlugin) plugin).handleSinglePage(resp.getRespText(Charset.forName("GB2312")), "http://travel.people.com.cn/n1/2019/0527/c41570-31103948.html");
//        System.out.println(JSON.toJSONString(news));
    }

    @Test
    public void test() throws InterruptedException {
        MainExecutor mainExecutor = new MainExecutor();
        mainExecutor.executor();
//        System.out.println(new Date(mainExecutor.initDelayTime()).getTime()/1000);

        TimeUnit.MINUTES.sleep(5);
    }
}
