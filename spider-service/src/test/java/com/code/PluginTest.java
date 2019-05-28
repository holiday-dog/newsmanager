package com.code;

import com.code.spider.plugin.ClientPlugin;
import com.code.spider.plugin.XinhuaEduPlugin;
import org.junit.Test;

public class PluginTest {
    @Test
    public void testt(){
        ClientPlugin plugin = new XinhuaEduPlugin();
//        System.out.println(plugin.genHostPrex("http://education.news.cn/2019-05/28/c_1210145273.htm"));
        System.out.println(plugin.spiderProcess(null));
    }
}
