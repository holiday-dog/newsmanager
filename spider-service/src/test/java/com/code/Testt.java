package com.code;

import com.alibaba.fastjson.JSON;
import com.code.common.utils.*;
import com.code.spider.plugin.ExtractorUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDateTime;

public class Testt {
    @Test
    public void testpage2() throws IOException {
        InputStream inputStream = Testt.class.getClassLoader().getResourceAsStream("test/page3.html");
        String page = IOUtils.getStringByInputStream(inputStream);

        String title = JsoupUtils.getText(page, "div[class~=text_title] h1").trim();
        String keyword = JsoupUtils.getAttr(page, "meta[name='keywords']", "content").get(0);
        String description = JsoupUtils.getAttr(page, "meta[name='description']", "content").get(0);
        String content = JsoupUtils.getElementsHtmlPage(page, "div#rwb_zw");
        if (StringUtils.isNotEmpty(content)) {
            content = JsoupUtils.removeElement(content, "div.edit");
        }

        String pubTime = JsoupUtils.getText(page, "div[class~=text_title] div.fl");
        pubTime = PatternUtils.groupOne(pubTime, "(\\d{4}.\\d{2}.\\d{2}.{1,2}\\d{2}:\\d{2})", 1);
        String source = JsoupUtils.getAttr(page, "meta[name='source']", "content").get(0);
        source = StringUtils.substringAfter(source, "来源：");
        String author = JsoupUtils.getText(page, "div[class~=text_title] p.author");
        if (StringUtils.isEmpty(author)) {
            author = JsoupUtils.getText(page, "div:contains(责编)");
            author = PatternUtils.groupOne(author, "\\(责编：([^\\(\\)]+)\\)", 1);
        }
        content = ExtractorUtils.extractRenminContent(page, null);

        System.out.println(keyword);
        System.out.println(title);
        System.out.println(description);
        System.out.println(content);
        System.out.println(DateUtils.parseDateTime(pubTime, "yyyy年MM月dd日HH:mm"));
        System.out.println(author);
        System.out.println(source);

    }

    @Test
    public void testpage() throws IOException {
        InputStream inputStream = Testt.class.getClassLoader().getResourceAsStream("test/page.html");
        String page = IOUtils.getStringByInputStream(inputStream);

        System.out.println(JSON.toJSONString(JsoupUtils.getAttr(page, "div#hideData0 ul.dataList li a img", "data-original")));
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
//        InputStream inputStream = Testt.class.getClassLoader().getResourceAsStream("test/page1.html");
        String page = IOUtils.stringByResource("test/page.json", null);
        page = JsonPathUtils.getValue(page, "$.topList[0].page");
//        System.out.println(ExtractorUtils.extractXinhua(page));
        System.out.println(ExtractorUtils.extractorXinhuaContent(page, ""));
//        System.out.println(page);
//        System.out.println(ExtractorUtils.extractXinhua(page));
//        System.out.println(ExtractorUtils.extractorXinhuaContent(page, "http://www.xinhuanet.com/politics/leaders/2019-05/31/c_1124569696.htm"));

//        System.out.println(JsoupUtils.getElementsHtml(page, "div[class~=swiper-container] div.swiper-wrapper div.swiper-slide"));
//        List<String> pages = JsoupUtils.getElementsHtml(page, "div#focusBoxBody div.txt ul li");
//        List<String> pageImgs = JsoupUtils.getElementsHtml(page, "div#focusBoxBody div.focusBoxWrap ul li img");
//        pages = JsoupUtils.getAttr(page, "ul#gd_content li a", "href");
//        pages = JsoupUtils.getAttr(page, "div#hpart2L a", "href");
//        pages = JsoupUtils.getAttr(page, "div#rmw_a ul.list14 li a", "href");
//        System.out.println(JSON.toJSONString(pages));
//        for (int i = 0; i < pages.size(); i++) {//http://edu.people.com.cn/
//            String s = pages.get(i);
//            System.out.println(s);
//            System.out.println(JsoupUtils.getAttr(s, "a", "href"));
//            System.out.println(ExtractorUtils.extractRenminHot(s, "http://www.people.com.cn/"));
//            System.out.println(JsoupUtils.getText(s, "div.show a"));
//            System.out.println(JsoupUtils.getAttr(s, "a img", "src").get(0));
//            System.out.println(JsoupUtils.getAttr(s, "a", "href").get(0));
//        }
        //JsoupUtils.removeElement(content, "table:has(img[src~=prev_page]")
//        String msg = JsoupUtils.getElementsHtmlPage(page, "table:has(img[src~=prev_page])");
//        String ss = JsoupUtils.replaceAttrAppendValue(msg, "img", "src", "http://");
//        System.out.println(msg);
//        System.out.println(ExtractorUtils.extractRenmin(page));
    }

    @Test
    public void test6() throws IOException {
        InputStream inputStream = Testt.class.getClassLoader().getResourceAsStream("test/page.html");
        String page = IOUtils.getStringByInputStream(inputStream, Charset.forName("GB2312"));


        String s = "jQuery112409162368214565164_1559465896910({\"status\":0,\"data\":{\"list\":[{\"DocID\":1210147250,\"Title\":\"职场干活的不如做PPT的？六成受访者反对重形式轻实质\",\"NodeId\":11109063,\"PubTime\":\"2019-05-30 08:44:45\",\"LinkUrl\":\"http://education.news.cn/2019-05/30/c_1210147250.htm\",\"Abstract\":\"工作中，不少职场人抱怨“累死累活不如PPT(演示文稿)做得好”。调查显示，63.7%的受访者认为汇报工作“重形式而轻实质”的做法，会使“桃李不言，下自成蹊”的文化美德淡化。\",\"keyword\":\"PPT,职场人,受访者,劣币驱逐良币,形式主义\",\"Editor\":\"王晓阳\",\"Author\":\"王品芝\",\"IsLink\":0,\"SourceName\":\"中国青年报\",\"PicLinks\":\"1210147250_1559176774041_title.jpg\",\"IsMoreImg\":0,\"imgarray\":[],\"SubTitle\":null,\"Attr\":63,\"m4v\":null,\"tarray\":[],\"uarray\":[],\"allPics\":[\"http://education.news.cn/titlepic/121014/1210147250_1559176774041_title0h.jpg\"],\"IntroTitle\":null,\"Ext1\":null,\"Ext2\":null,\"Ext3\":null,\"Ext4\":null,\"Ext5\":null,\"Ext6\":null,\"Ext7\":null,\"Ext8\":null,\"Ext9\":null,\"Ext10\":null},{\"DocID\":1210145568,\"Title\":\"“创新”作业让家长苦不堪言！警惕奇葩作业穿上创新型教育马甲\",\"NodeId\":11109063,\"PubTime\":\"2019-05-28 11:14:51\",\"LinkUrl\":\"http://education.news.cn/2019-05/28/c_1210145568.htm\",\"Abstract\":\"与自家房间合影，折树枝学插花，代购手抄报作业……近日，一些小学、幼儿园老师留的作业引发公众关注和热议。内容让家长不理解，直呼“奇葩”，还有的作业“超纲”，直接由家长代劳完成。\",\"keyword\":\"创新型教育,奇葩,家长委员会,开放性教育,教育方式\",\"Editor\":\"陈梦谣\",\"Author\":\"孟含琪 金津秀\",\"IsLink\":0,\"SourceName\":\"半月谈网\",\"PicLinks\":\"1210145568_1559013250876_title.jpg\",\"IsMoreImg\":0,\"imgarray\":[],\"SubTitle\":null,\"Attr\":63,\"m4v\":null,\"tarray\":[],\"uarray\":[],\"allPics\":[\"http://education.news.cn/titlepic/121014/1210145568_1559013250876_title0h.jpg\"],\"IntroTitle\":null,\"Ext1\":null,\"Ext2\":null,\"Ext3\":null,\"Ext4\":null,\"Ext5\":null,\"Ext6\":null,\"Ext7\":null,\"Ext8\":null,\"Ext9\":null,\"Ext10\":null},{\"DocID\":1210145292,\"Title\":\"关爱服务留守儿童和困境儿童 村里配上“儿童主任”\",\"NodeId\":11109063,\"PubTime\":\"2019-05-28 08:35:28\",\"LinkUrl\":\"http://education.news.cn/2019-05/28/c_1210145292.htm\",\"Abstract\":\"2019年，中央补助中、东、西部地区孤儿基本生活费标准从200元、300元、400元每人每月提高到300元、450元、600元每人每月。\",\"keyword\":\"留守儿童,意见,救助保护,儿童主任,监护责任\",\"Editor\":\"陈梦谣\",\"Author\":null,\"IsLink\":0,\"SourceName\":\"人民日报\",\"PicLinks\":\"1210145292_1559003671697_title.jpg\",\"IsMoreImg\":0,\"imgarray\":[],\"SubTitle\":null,\"Attr\":63,\"m4v\":null,\"tarray\":[],\"uarray\":[],\"allPics\":[\"http://education.news.cn/titlepic/121014/1210145292_1559003671697_title0h.jpg\"],\"IntroTitle\":null,\"Ext1\":null,\"Ext2\":null,\"Ext3\":null,\"Ext4\":null,\"Ext5\":null,\"Ext6\":null,\"Ext7\":null,\"Ext8\":null,\"Ext9\":null,\"Ext10\":null},{\"DocID\":1210145273,\"Title\":\"体验垦荒、收集家书、监测雾霾……思政课有料又有趣\",\"NodeId\":11109063,\"PubTime\":\"2019-05-28 08:25:43\",\"LinkUrl\":\"http://education.news.cn/2019-05/28/c_1210145273.htm\",\"Abstract\":\"目前，各地各校积极行动、统筹推进大中小学思政课一体化建设。德育教育不仅在课堂里，还在形式新颖的活动中，为学生的成长打好底色。\",\"keyword\":\"思政,思想政治理论课,粉尘仪,上海市杨浦高级中学,德育方式\",\"Editor\":\"陈梦谣\",\"Author\":\"丁雅诵\",\"IsLink\":0,\"SourceName\":\"人民日报\",\"PicLinks\":\"1210145273_1559003044685_title.jpg\",\"IsMoreImg\":0,\"imgarray\":[],\"SubTitle\":null,\"Attr\":63,\"m4v\":null,\"tarray\":[],\"uarray\":[],\"allPics\":[\"http://education.news.cn/titlepic/121014/1210145273_1559003044685_title0h.jpg\"],\"IntroTitle\":null,\"Ext1\":null,\"Ext2\":null,\"Ext3\":null,\"Ext4\":null,\"Ext5\":null,\"Ext6\":null,\"Ext7\":null,\"Ext8\":null,\"Ext9\":null,\"Ext10\":null},{\"DocID\":1210144950,\"Title\":\"[亲子育儿]建议:劳动教育需把握年龄特点\",\"NodeId\":11109063,\"PubTime\":\"2019-05-27 16:08:30\",\"LinkUrl\":\"http://education.news.cn/2019-05/27/c_1210144950.htm\",\"Abstract\":\"早在2015年，教育部就发布了《关于加强中小学劳动教育的意见》。一些地方甚至将孩子的家务活等劳动情况，记入学生综合素质档案，作为其升学、评优的重要参考。\",\"keyword\":null,\"Editor\":\"王琦\",\"Author\":null,\"IsLink\":0,\"SourceName\":\"北京晚报\",\"PicLinks\":\"1210144950_1558944446833_title.jpg\",\"IsMoreImg\":0,\"imgarray\":[],\"SubTitle\":null,\"Attr\":63,\"m4v\":null,\"tarray\":[],\"uarray\":[],\"allPics\":[\"http://education.news.cn/titlepic/121014/1210144950_1558944446833_title0h.jpg\"],\"IntroTitle\":null,\"Ext1\":null,\"Ext2\":null,\"Ext3\":null,\"Ext4\":null,\"Ext5\":null,\"Ext6\":null,\"Ext7\":null,\"Ext8\":null,\"Ext9\":null,\"Ext10\":null},{\"DocID\":1210144929,\"Title\":\"[职通车]学校投资 高一学生开“公司”\",\"NodeId\":11109063,\"PubTime\":\"2019-05-27 15:50:39\",\"LinkUrl\":\"http://education.news.cn/2019-05/27/c_1210144929.htm\",\"Abstract\":\"在学校“投资”下，高一学生开办了6家“创客公司”，学生不仅自主研发、设计产品，甚至自己寻找生产厂家，生产制作校园元素的文创产品，当起了校园小创客。\",\"keyword\":\"创客\",\"Editor\":\"王琦\",\"Author\":null,\"IsLink\":0,\"SourceName\":\"北京晚报\",\"PicLinks\":\"1210144929_1558943389925_title.jpg\",\"IsMoreImg\":0,\"imgarray\":[],\"SubTitle\":null,\"Attr\":63,\"m4v\":null,\"tarray\":[],\"uarray\":[],\"allPics\":[\"http://education.news.cn/titlepic/121014/1210144929_1558943389925_title0h.jpg\"],\"IntroTitle\":null,\"Ext1\":null,\"Ext2\":null,\"Ext3\":null,\"Ext4\":null,\"Ext5\":null,\"Ext6\":null,\"Ext7\":null,\"Ext8\":null,\"Ext9\":null,\"Ext10\":null},{\"DocID\":1210144507,\"Title\":\"小伙11岁放言造飞机 20岁驾驶自己造的飞机试飞成功\",\"NodeId\":11109063,\"PubTime\":\"2019-05-27 09:41:31\",\"LinkUrl\":\"http://education.news.cn/2019-05/27/c_1210144507.htm\",\"Abstract\":\"虽然试飞时间只有几分钟，但等待这一刻，唐海峰用了十年的时间。\",\"keyword\":null,\"Editor\":\"王琦\",\"Author\":null,\"IsLink\":0,\"SourceName\":\"成都商报\",\"PicLinks\":\"1210144507_1558921201980_title.jpg\",\"IsMoreImg\":0,\"imgarray\":[],\"SubTitle\":null,\"Attr\":63,\"m4v\":null,\"tarray\":[],\"uarray\":[],\"allPics\":[\"http://education.news.cn/titlepic/121014/1210144507_1558921201980_title0h.jpg\"],\"IntroTitle\":null,\"Ext1\":null,\"Ext2\":null,\"Ext3\":null,\"Ext4\":null,\"Ext5\":null,\"Ext6\":null,\"Ext7\":null,\"Ext8\":null,\"Ext9\":null,\"Ext10\":null},{\"DocID\":1210144512,\"Title\":\"[考动力]北京延庆中小学生将100%上冰上雪\",\"NodeId\":11109063,\"PubTime\":\"2019-05-27 09:30:50\",\"LinkUrl\":\"http://education.news.cn/2019-05/27/c_1210144512.htm\",\"Abstract\":\"到2025年，新增学前学位3960个，适龄儿童入园率稳定在95%以上；新增3400个中小学学位；引入一至两所高校；中小学生100%参与冰雪运动……25日，延庆区召开教育大会，公布2019年至2025年教育发展目标。\",\"keyword\":\"延庆,冰雪运动,北京,学位\",\"Editor\":\"王琦\",\"Author\":null,\"IsLink\":0,\"SourceName\":\"北京日报\",\"PicLinks\":\"1210144512_1558920595492_title.jpg\",\"IsMoreImg\":0,\"imgarray\":[],\"SubTitle\":null,\"Attr\":63,\"m4v\":null,\"tarray\":[],\"uarray\":[],\"allPics\":[\"http://education.news.cn/titlepic/121014/1210144512_1558920595492_title0h.jpg\"],\"IntroTitle\":null,\"Ext1\":null,\"Ext2\":null,\"Ext3\":null,\"Ext4\":null,\"Ext5\":null,\"Ext6\":null,\"Ext7\":null,\"Ext8\":null,\"Ext9\":null,\"Ext10\":null},{\"DocID\":1210144477,\"Title\":\"原是低保户 如今名厨师 青海职业教育助力脱贫\",\"NodeId\":11109063,\"PubTime\":\"2019-05-27 09:02:27\",\"LinkUrl\":\"http://education.news.cn/2019-05/27/c_1210144477.htm\",\"Abstract\":\"原本是低保户，如今是名厨师。牛建兴的转变，连他自己都没想到。在青海省互助土族自治县乐业酒店，牛建兴做的菜样式新颖、色香味俱全，引得众多客人慕名而来。\",\"keyword\":\"职业教育集团,职业教育改革,集团化办学,中职学校,加快推进\",\"Editor\":\"陈梦谣\",\"Author\":\"姜 峰 王 梅\",\"IsLink\":0,\"SourceName\":\"人民日报\",\"PicLinks\":\"1210144477_1558918842595_title.png\",\"IsMoreImg\":0,\"imgarray\":[],\"SubTitle\":null,\"Attr\":63,\"m4v\":null,\"tarray\":[],\"uarray\":[],\"allPics\":[\"http://education.news.cn/titlepic/121014/1210144477_1558918842595_title0h.png\"],\"IntroTitle\":null,\"Ext1\":null,\"Ext2\":null,\"Ext3\":null,\"Ext4\":null,\"Ext5\":null,\"Ext6\":null,\"Ext7\":null,\"Ext8\":null,\"Ext9\":null,\"Ext10\":null},{\"DocID\":1210144432,\"Title\":\"浙江省高校专业设置发生变化 66个新增专业覆盖新热点\",\"NodeId\":11109063,\"PubTime\":\"2019-05-27 08:44:14\",\"LinkUrl\":\"http://education.news.cn/2019-05/27/c_1210144432.htm\",\"Abstract\":\"下个月就要高考了，除了积极备考，考生和家长们最关心的内容之一，或许就是各大高校的专业设置。\",\"keyword\":\"专业设置,高考,大数据\",\"Editor\":\"王琦\",\"Author\":null,\"IsLink\":0,\"SourceName\":\"钱江晚报\",\"PicLinks\":\"1210144432_1558918308918_title.jpg\",\"IsMoreImg\":0,\"imgarray\":[],\"SubTitle\":null,\"Attr\":63,\"m4v\":null,\"tarray\":[],\"uarray\":[],\"allPics\":[\"http://education.news.cn/titlepic/121014/1210144432_1558918308918_title0h.jpg\"],\"IntroTitle\":null,\"Ext1\":null,\"Ext2\":null,\"Ext3\":null,\"Ext4\":null,\"Ext5\":null,\"Ext6\":null,\"Ext7\":null,\"Ext8\":null,\"Ext9\":null,\"Ext10\":null}]},\"totalnum\":1000}\n" +
                ")";
        System.out.println(PatternUtils.groupOne(s, "jQuery\\d+\\_\\d+\\((.*)", 1));
        String ss = PatternUtils.groupOne(s, "jQuery\\d+\\_\\d+\\((.*)", 1);
        System.out.println(ss);
        System.out.println(JsonPathUtils.getValueList(ss, "$.data.list[*].LinkUrl"));
    }

    @Test
    public void test7() {
        String s = "BCore.prototype.options.gid=\"87205254007bf95200005f7b0330b2d35ced1e02\";";

        System.out.println(RandomUtils.nextString().length());
        System.out.println("5b1bd51383a2571b294eafa41c789a28".length());
        System.out.println(DateUtils.parseDateTime("2019年05月12日 08:51:23", "yyyy年MM月dd日 HH:mm:ss"));
//        System.out.println(PatternUtils.groupOne(s, "gid=\"(\\w+)\"", 1));
    }
}
