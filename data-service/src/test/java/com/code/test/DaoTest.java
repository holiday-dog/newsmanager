package com.code.test;

import com.alibaba.fastjson.JSON;
import com.code.common.bean.HotNews;
import com.code.common.enums.Modules;
import com.code.common.enums.NewsType;
import com.code.common.utils.IOUtils;
import com.code.common.utils.JsonPathUtils;
import com.code.common.utils.RandomUtils;
import com.code.data.DataApplication;
import com.code.data.beans.NewsInfo;
import com.code.data.dao.NewsContentInfoMapper;
import com.code.data.dao.NewsHotInfoMapper;
import com.code.data.dao.NewsInfoMapper;
import com.code.data.mq.SpiderDataHandler;
import com.code.data.service.NewsHotInfoService;
import com.code.data.service.NewsInfoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DataApplication.class})
@MapperScan("com.code.data.beans")
public class DaoTest {
    @Resource
    private NewsInfoMapper newsInfoMapper;
    @Resource
    private NewsContentInfoMapper contentInfoMapper;
    @Autowired
    private NewsInfoService newsInfoService;
    @Autowired
    private NewsHotInfoService hotInfoService;

    @Resource
    private NewsHotInfoMapper hotInfoMapper;
    @Autowired
    private SpiderDataHandler dataHandler;

    String page;


    @Test
    public void testa() throws ExecutionException, InterruptedException {
//        System.out.println(JSON.toJSONString(hotInfoService.queryHotNewsList(Modules.EDUCATION, 2)));
        System.out.println(dataHandler.handler(page));
    }

    public void test() {
//        List<News> newsList = JsonPathUtils.getObjList(page, "$.newestEduList[*]", News.class);
        page = JsonPathUtils.jsonArray(page, "$.hotEduList[*]");
        System.out.println(page);
        List<HotNews> newsList = JSON.parseArray(page, HotNews.class);
        System.out.println(JSON.toJSONString(newsList));

//        News news = JSON.parseObject(JSON.toJSONString(map), News.class);
//        System.out.println(JSON.toJSONString(newsList));

        hotInfoService.buildAndSave(newsList, Modules.EDUCATION);

//        List<News> newsList = newsInfoService.queryList(Modules.TRAVEL, NewsType.HISTORY);
//        System.out.println(JSON.toJSONString(newsList));
//        System.out.println(JSON.toJSONString(newsInfoService.queryNews(newsList.get(0).getSign())));

//        NewsInfo newsInfo = new NewsInfo();
//        BeanUtils.copyProperties(news, newsInfo);
//        System.out.println(JSON.toJSONString(newsInfo));
//        List<NewsInfo> all = new ArrayList<>();
//        NewsInfo newsInfo = null;
//        newsInfo = new NewsInfo();
//        newsInfo.setTitle("aaa");
//        all.add(newsInfo);
//        newsInfo = new NewsInfo();
//        newsInfo.setTitle("bbb");
//        all.add(newsInfo);
//        newsInfo = new NewsInfo();
//        newsInfo.setTitle("ccc");
//        all.add(newsInfo);
//        newsInfoMapper.insertList(all);


//        System.out.println(JSON.toJSONString(newsInfoMapper.selectByPrimaryKey(1L), SerializerFeature.WriteDateUseDateFormat));
    }


    @Before
    public void before() throws IOException {
        InputStream inputStream = DaoTest.class.getClassLoader().getResourceAsStream("test/page.json");
        page = IOUtils.getStringByInputStream(inputStream);
    }

    @Test
    public void testt() {
//        Map map = (Map) JsonPathUtils.getValue(page, "$.historyTravelList[0]");
//        System.out.println(JSON.toJSONString(map));
//        System.out.println(BeanUtils.mapToBean(map, News.class));

//        System.out.println(DateUtils.parseDateTime("2019-05-30T08:42:00", "yyyy-MM-dd'T'HH:mm:ss"));


//        List<NewsContentInfo> contentInfos = new ArrayList<>();
//        NewsContentInfo contentInfo = null;
//        contentInfo = new NewsContentInfo();
//        contentInfo.setNewId(2L);
//        contentInfos.add(contentInfo);
//        contentInfo = new NewsContentInfo();
//        contentInfo.setNewId(3L);
//        contentInfos.add(contentInfo);
//
//        contentInfoMapper.insertList(contentInfos);

        NewsInfo info = new NewsInfo();
        info.setTitle("aaaa");
        info.setSign(RandomUtils.nextString());
        info.setPubTime(LocalDateTime.now());
        newsInfoMapper.insert(info);

    }
}
