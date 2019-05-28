package com.code;

import com.alibaba.fastjson.JSON;
import com.code.common.bean.News;
import com.code.common.crawl.WebClient;
import com.code.common.crawl.WebRequest;
import com.code.common.crawl.WebResponse;
import com.code.spider.plugin.ClientPlugin;
import com.code.spider.plugin.XinhuaEduPlugin;
import com.code.spider.plugin.XinhuaSciencePlugin;
import com.code.spider.plugin.XinhuaTravelPlugin;
import org.junit.Test;

import java.io.IOException;

public class PluginTest {
    @Test
    public void testt() {
        ClientPlugin plugin = null;
//        plugin = new XinhuaEduPlugin();
//        plugin = new XinhuaTravelPlugin();
        plugin = new XinhuaSciencePlugin();
//        System.out.println(plugin.genHostPrex("http://education.news.cn/2019-05/28/c_1210145273.htm"));
        System.out.println(plugin.spiderProcess(null));
    }

    @Test
    public void test1() throws IOException {
        ClientPlugin plugin = new XinhuaTravelPlugin();

        WebRequest req = new WebRequest("http://www.xinhuanet.com/travel/2019-05/27/c_1124544385.htm");
        WebResponse resp = WebClient.buildDefaultClient().build().execute(req);
        News news = ((XinhuaTravelPlugin) plugin).handleMultiPage(resp.getRespText(), "http://www.xinhuanet.com/travel/2019-05/27/c_1124544385.htm");
        System.out.println(JSON.toJSONString(news));
    }
}
