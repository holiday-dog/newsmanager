package com.code;

import com.code.common.utils.*;
import com.code.spider.plugin.ExtractorUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.List;

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

    public void testpage() throws IOException {
        InputStream inputStream = Testt.class.getClassLoader().getResourceAsStream("test/page3.html");
        String page = IOUtils.getStringByInputStream(inputStream);

//        for(String url: JsoupUtils.getAttr(page, "ul#gd_content li a", "href")){
//            System.out.println(url);
//        }

//        String ss = PatternUtils.groupOne(page, "jQuery\\d+_\\d+\\(([^\\(\\)]+)\\)", 1);
//        List<String> lists= JsonPathUtils.getValueList(ss, "$.data.list[*].LinkUrl");
//        System.out.println(JSON.toJSONString(lists));

//        String title = JsoupUtils.getText(page, "span#title").trim();
        String title = JsoupUtils.getText(page, "div.h-title").trim();
        String keyword = JsoupUtils.getAttr(page, "meta[name='keywords']", "content").get(0);
        String description = JsoupUtils.getAttr(page, "meta[name='description']", "content").get(0);
        description = StringUtils.substringAfter(description, "---");
//        String content = JsoupUtils.getElementsHtmlPage(page, "span#content p");
        String content = JsoupUtils.getElementsHtmlPage(page, "div#p-detail p");
//        String pubTime = JsoupUtils.getText(page, "span#pubtime");
        String pubTime = JsoupUtils.getText(page, "span.h-time");
        String source = JsoupUtils.getText(page, "em#source");
        String author = JsoupUtils.getText(page, "span:contains(责任编辑)");
        System.out.println(author);
        author = PatternUtils.groupOne(author, "责任编辑:?：?([^\\<\\]]+)", 1);
//        List<String> images = JsoupUtils.getAttr(content, "img", "src");
//        if (!CollectionUtils.isEmpty(images)) {
//            content = JsoupUtils.replaceAttrAppendValue(content, "img", "src", "http://");
//        }
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
//        InputStream inputStream = Testt.class.getClassLoader().getResourceAsStream("test/page1.html");
        String page = IOUtils.stringByResource("test/page2.html", null);
//        System.out.println(page);
        System.out.println(ExtractorUtils.extractXinhua(page));
        System.out.println(ExtractorUtils.extractorXinhuaContent(page, "http://www.xinhuanet.com/politics/leaders/2019-05/31/c_1124569696.htm"));

//        System.out.println(JsoupUtils.getElementsHtml(page, "div[class~=swiper-container] div.swiper-wrapper div.swiper-slide"));
        List<String> pages = JsoupUtils.getElementsHtml(page, "div#focusBoxBody div.txt ul li");
//        List<String> pageImgs = JsoupUtils.getElementsHtml(page, "div#focusBoxBody div.focusBoxWrap ul li img");
//        pages = JsoupUtils.getAttr(page, "ul#gd_content li a", "href");
//        pages = JsoupUtils.getAttr(page, "div#hpart2L a", "href");
//        pages = JsoupUtils.getAttr(page, "div#rmw_a ul.list14 li a", "href");
//        System.out.println(JSON.toJSONString(pages));
        for (int i = 0; i < pages.size(); i++) {//http://edu.people.com.cn/
            String s = pages.get(i);
            System.out.println(s);
//            System.out.println(JsoupUtils.getAttr(s, "a", "href"));
//            System.out.println(ExtractorUtils.extractRenminHot(s, "http://www.people.com.cn/"));
//            System.out.println(JsoupUtils.getText(s, "div.show a"));
//            System.out.println(JsoupUtils.getAttr(s, "a img", "src").get(0));
//            System.out.println(JsoupUtils.getAttr(s, "a", "href").get(0));
        }
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


        String s = "jQuery11240351351899117859_1559445982153({\"status\":0,\"data\":{\"list\":[{\"DocID\":1210144378,\"Title\":\"小伙回家乡陪伴父亲 温馨生活视频打动网友\",\"NodeId\":11109063,\"PubTime\":\"2019-05-27 08:22:23\",\"LinkUrl\":\"http://education.news.cn/2019-05/27/c_1210144378.htm\",\"Abstract\":\"镜头下的一父一子，儿子浓眉大眼、爱笑，父亲虽然眼盲但面目慈祥，两人或吃着简单的餐饭，或在室外编织蚕架、干农活……这些有关日常生活场景的视频片段，连日来在网上走红。\",\"keyword\":\"暖心\",\"Editor\":\"王琦\",\"Author\":null,\"IsLink\":0,\"SourceName\":\"北京青年报\",\"PicLinks\":\"1210144378_1558916481619_title.jpg\",\"IsMoreImg\":0,\"imgarray\":[],\"SubTitle\":null,\"Attr\":63,\"m4v\":null,\"tarray\":[],\"uarray\":[],\"allPics\":[\"http://education.news.cn/titlepic/121014/1210144378_1558916481619_title0h.jpg\"],\"IntroTitle\":null,\"Ext1\":null,\"Ext2\":null,\"Ext3\":null,\"Ext4\":null,\"Ext5\":null,\"Ext6\":null,\"Ext7\":null,\"Ext8\":null,\"Ext9\":null,\"Ext10\":null},{\"DocID\":1210142509,\"Title\":\"中高考应考健康指南：心理医生教你如何化解考试焦虑\",\"NodeId\":11109063,\"PubTime\":\"2019-05-24 08:59:21\",\"LinkUrl\":\"http://education.news.cn/2019-05/24/c_1210142509.htm\",\"Abstract\":\"考试毕业季临近，去医院看心理医生的青少年和家长多了起来。考后焦虑一般表现为：考完试后着急对答案，每天坐卧不安地等着成绩公布。王彦玲医生说，考试焦虑是一种正常的状态，每个人都会有，说明你对考试的重视。\",\"keyword\":\"考试情境,考试焦虑,家长,心理问题,心理医生\",\"Editor\":\"王晓阳\",\"Author\":\"张蕾\",\"IsLink\":0,\"SourceName\":\"中国青年报\",\"PicLinks\":\"1210142509_1558659493407_title.jpg\",\"IsMoreImg\":0,\"imgarray\":[],\"SubTitle\":null,\"Attr\":63,\"m4v\":null,\"tarray\":[],\"uarray\":[],\"allPics\":[\"http://education.news.cn/titlepic/121014/1210142509_1558659493407_title0h.jpg\"],\"IntroTitle\":null,\"Ext1\":null,\"Ext2\":null,\"Ext3\":null,\"Ext4\":null,\"Ext5\":null,\"Ext6\":null,\"Ext7\":null,\"Ext8\":null,\"Ext9\":null,\"Ext10\":null},{\"DocID\":1210140696,\"Title\":\"8岁弟弟实力宠姐 为25岁姐姐捐献造血干细胞\",\"NodeId\":11109063,\"PubTime\":\"2019-05-22 14:20:12\",\"LinkUrl\":\"http://education.news.cn/2019-05/22/c_1210140696.htm\",\"Abstract\":\"“为了救我，一家人都去做了配型检查，最后发现弟弟和我配对完全吻合。”小敏说，虽然岁数差很多，但自己和弟弟感情很好，“当时医生和弟弟说，抽点血给姐姐，她就能活命了，弟弟没有犹豫就说‘好’”。\",\"keyword\":\"捐献造血干细胞\",\"Editor\":\"王琦\",\"Author\":null,\"IsLink\":0,\"SourceName\":\"福州晚报\",\"PicLinks\":\"1210140696_1558505578881_title.jpg\",\"IsMoreImg\":0,\"imgarray\":[],\"SubTitle\":null,\"Attr\":63,\"m4v\":null,\"tarray\":[],\"uarray\":[],\"allPics\":[\"http://education.news.cn/titlepic/121014/1210140696_1558505578881_title0h.jpg\"],\"IntroTitle\":null,\"Ext1\":null,\"Ext2\":null,\"Ext3\":null,\"Ext4\":null,\"Ext5\":null,\"Ext6\":null,\"Ext7\":null,\"Ext8\":null,\"Ext9\":null,\"Ext10\":null},{\"DocID\":1210140388,\"Title\":\"老师患病插鼻饲管 女儿绘出妈妈最美最勇敢的样子\",\"NodeId\":11109063,\"PubTime\":\"2019-05-22 09:54:33\",\"LinkUrl\":\"http://education.news.cn/2019-05/22/c_1210140388.htm\",\"Abstract\":\"饭菜上桌时，庭庭都用渴望的眼神看着田静，希望妈妈也能吃一口。甚至在新年、圣诞节、生日，她许下的愿望都是：妈妈和我们一起吃顿饭。\",\"keyword\":\"大学教师,克罗恩病\",\"Editor\":\"王琦\",\"Author\":null,\"IsLink\":0,\"SourceName\":\"钱江晚报\",\"PicLinks\":\"1210140388_1558490030334_title.jpg\",\"IsMoreImg\":0,\"imgarray\":[],\"SubTitle\":null,\"Attr\":63,\"m4v\":null,\"tarray\":[],\"uarray\":[],\"allPics\":[\"http://education.news.cn/titlepic/121014/1210140388_1558490030334_title0h.jpg\"],\"IntroTitle\":null,\"Ext1\":null,\"Ext2\":null,\"Ext3\":null,\"Ext4\":null,\"Ext5\":null,\"Ext6\":null,\"Ext7\":null,\"Ext8\":null,\"Ext9\":null,\"Ext10\":null},{\"DocID\":1210140376,\"Title\":\"17岁盲女用钢琴“看”世界 用耳朵“看”电影\",\"NodeId\":11109063,\"PubTime\":\"2019-05-22 09:47:38\",\"LinkUrl\":\"http://education.news.cn/2019-05/22/c_1210140376.htm\",\"Abstract\":\"熊翎好第一次知道自己与普通孩子不一样，她哭着跑回家问母亲，为什么自己和别人不一样。母亲告诉她，“没事，让医生吹一下就能看见了。”\",\"keyword\":\"失明,盲童,钢琴\",\"Editor\":\"王琦\",\"Author\":null,\"IsLink\":0,\"SourceName\":\"成都商报\",\"PicLinks\":\"1210140376_1558489494261_title.jpg\",\"IsMoreImg\":0,\"imgarray\":[],\"SubTitle\":null,\"Attr\":63,\"m4v\":null,\"tarray\":[],\"uarray\":[],\"allPics\":[\"http://education.news.cn/titlepic/121014/1210140376_1558489494261_title0h.jpg\"],\"IntroTitle\":null,\"Ext1\":null,\"Ext2\":null,\"Ext3\":null,\"Ext4\":null,\"Ext5\":null,\"Ext6\":null,\"Ext7\":null,\"Ext8\":null,\"Ext9\":null,\"Ext10\":null},{\"DocID\":1210140341,\"Title\":\"南开大学发布40条本科教改创新举措\",\"NodeId\":11109063,\"PubTime\":\"2019-05-22 09:28:04\",\"LinkUrl\":\"http://education.news.cn/2019-05/22/c_1210140341.htm\",\"Abstract\":\"针对建设一流本科教育的重点难点问题，南开大学正式发布《南开大学一流本科教育质量提升行动计划（2019-2021年）》，即“南开40条”，从十个方面提出40条创新举措，打出本科教育改革“组合拳”。\",\"keyword\":\"南开大学,本科教育,教改,以本为本,有效教学\",\"Editor\":\"陈梦谣\",\"Author\":null,\"IsLink\":0,\"SourceName\":\"中国青年报\",\"PicLinks\":\"1210140341_1558488431733_title.jpg\",\"IsMoreImg\":0,\"imgarray\":[],\"SubTitle\":null,\"Attr\":63,\"m4v\":null,\"tarray\":[],\"uarray\":[],\"allPics\":[\"http://education.news.cn/titlepic/121014/1210140341_1558488431733_title0h.jpg\"],\"IntroTitle\":null,\"Ext1\":null,\"Ext2\":null,\"Ext3\":null,\"Ext4\":null,\"Ext5\":null,\"Ext6\":null,\"Ext7\":null,\"Ext8\":null,\"Ext9\":null,\"Ext10\":null},{\"DocID\":1210140288,\"Title\":\"留学生新版学历认证系统启用\",\"NodeId\":11109063,\"PubTime\":\"2019-05-22 09:07:57\",\"LinkUrl\":\"http://education.news.cn/2019-05/22/c_1210140288.htm\",\"Abstract\":\"今日开始，留学生需通过新版国（境）外学历学位认证系统进行学历学位认证，认证结果将以电子证照形式发布，不再提供纸质认证结果。旧版认证系统将不再接收新认证申请。\",\"keyword\":\"学历学位,留学网,认证系统,留学生,学历认证\",\"Editor\":\"陈梦谣\",\"Author\":\"方怡君\",\"IsLink\":0,\"SourceName\":\"新京报\",\"PicLinks\":\"1210140288_1558487177337_title.jpg\",\"IsMoreImg\":0,\"imgarray\":[],\"SubTitle\":null,\"Attr\":63,\"m4v\":null,\"tarray\":[],\"uarray\":[],\"allPics\":[\"http://education.news.cn/titlepic/121014/1210140288_1558487177337_title0h.jpg\"],\"IntroTitle\":null,\"Ext1\":null,\"Ext2\":null,\"Ext3\":null,\"Ext4\":null,\"Ext5\":null,\"Ext6\":null,\"Ext7\":null,\"Ext8\":null,\"Ext9\":null,\"Ext10\":null},{\"DocID\":1210140198,\"Title\":\"让孩子带笑回家\",\"NodeId\":11109063,\"PubTime\":\"2019-05-22 08:48:50\",\"LinkUrl\":\"http://education.news.cn/2019-05/22/c_1210140198.htm\",\"Abstract\":\"一个3岁儿童输液时哭了。一位年轻女医生安慰说：“宝宝不哭。”孩子说：“我没有哭，只是眼睛下雨了。”这让她瞬间深爱上儿科，儿科是这么美好！她就是首都医科大学附属北京儿童医院急诊科主任王荃。\",\"keyword\":\"王荃,首都医科大学附属北京儿童医院,粒细胞减少,孩子,房室传导阻滞\",\"Editor\":\"陈梦谣\",\"Author\":\"王君平\",\"IsLink\":0,\"SourceName\":\"人民日报\",\"PicLinks\":\"1210140198_1558486089065_title.jpg\",\"IsMoreImg\":0,\"imgarray\":[],\"SubTitle\":null,\"Attr\":63,\"m4v\":null,\"tarray\":[],\"uarray\":[],\"allPics\":[\"http://education.news.cn/titlepic/121014/1210140198_1558486089065_title0h.jpg\"],\"IntroTitle\":null,\"Ext1\":null,\"Ext2\":null,\"Ext3\":null,\"Ext4\":null,\"Ext5\":null,\"Ext6\":null,\"Ext7\":null,\"Ext8\":null,\"Ext9\":null,\"Ext10\":null},{\"DocID\":1210138147,\"Title\":\"校园故事：年轻学者冲刺高校“科技力”\",\"NodeId\":11109063,\"PubTime\":\"2019-05-20 09:44:49\",\"LinkUrl\":\"http://education.news.cn/2019-05/20/c_1210138147.htm\",\"Abstract\":\"“在所有令人心碎的劳作中，开道是最糟的。”科研，算是“开道”中最富挑战性的一种脑力劳作。尤其在未知之地，你可能不知道下一秒钟，自己的脚会在何处，将会踏向何方。\",\"keyword\":\"科研\",\"Editor\":\"王琦\",\"Author\":null,\"IsLink\":0,\"SourceName\":\"中国青年报\",\"PicLinks\":\"1210138147_1558316652981_title.jpg\",\"IsMoreImg\":0,\"imgarray\":[],\"SubTitle\":null,\"Attr\":63,\"m4v\":null,\"tarray\":[],\"uarray\":[],\"allPics\":[\"http://education.news.cn/titlepic/121013/1210138147_1558316652981_title0h.jpg\"],\"IntroTitle\":null,\"Ext1\":null,\"Ext2\":null,\"Ext3\":null,\"Ext4\":null,\"Ext5\":null,\"Ext6\":null,\"Ext7\":null,\"Ext8\":null,\"Ext9\":null,\"Ext10\":null},{\"DocID\":1210138129,\"Title\":\"台下台上[组图]\",\"NodeId\":11109063,\"PubTime\":\"2019-05-20 09:39:44\",\"LinkUrl\":\"http://education.news.cn/2019-05/20/c_1210138129.htm\",\"Abstract\":\"这些青少年在台上的表演只有几分钟，台下的努力和期待却酝酿了很久。在文化课学习之外，艺术训练、美学教育对这些学生内在气质的塑造也是独特而持续的。\",\"keyword\":null,\"Editor\":\"王琦\",\"Author\":null,\"IsLink\":0,\"SourceName\":\"中国青年报\",\"PicLinks\":\"1210138129_1558316331228_title.jpg\",\"IsMoreImg\":0,\"imgarray\":[],\"SubTitle\":null,\"Attr\":63,\"m4v\":null,\"tarray\":[],\"uarray\":[],\"allPics\":[\"http://education.news.cn/titlepic/121013/1210138129_1558316331228_title0h.jpg\"],\"IntroTitle\":null,\"Ext1\":null,\"Ext2\":null,\"Ext3\":null,\"Ext4\":null,\"Ext5\":null,\"Ext6\":null,\"Ext7\":null,\"Ext8\":null,\"Ext9\":null,\"Ext10\":null}]},\"totalnum\":1000}\n" +
                ")";
        System.out.println(PatternUtils.groupOne(page.trim(), "jQuery\\d+_\\d+\\(\\n?([^\\(\\)]+)\\n?\\)", 1));
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
