package com.code.data.mq;

import com.alibaba.fastjson.JSON;
import com.code.common.bean.HotNews;
import com.code.common.bean.News;
import com.code.common.enums.Modules;
import com.code.common.utils.JsonPathUtils;
import com.code.data.service.NewsHotInfoService;
import com.code.data.service.NewsInfoService;
import com.code.data.service.ProcessInfoService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

@Component
public class SpiderDataHandler {
    @Autowired
    private NewsInfoService newsInfoService;
    @Autowired
    private NewsHotInfoService hotInfoService;
    @Autowired
    private ProcessInfoService processInfoService;
    private ExecutorService executorService = Executors.newFixedThreadPool(2);
    private Logger logger = LoggerFactory.getLogger(SpiderDataHandler.class);

    public boolean handler(String spiderData) throws ExecutionException, InterruptedException {
        logger.info("data:{}", spiderData);

        Modules modules = Modules.parse(JsonPathUtils.getValue(spiderData, "$.moduleType"));
        String spiderWeb = JsonPathUtils.getValue(spiderData, "$.spiderWebsite");
        logger.info("web:{}, modules:{}", spiderWeb, modules.getMsg());

        Callable<Integer> statusCallable = new Callable() {
            @Override
            public Object call() throws Exception {
                try {
                    processInfoService.buildAneSave(spiderData);
                    return 1;
                } catch (Exception e) {
                    logger.error("hotCallable error, msg:{}", e);
                    return 0;
                }
            }
        };

        Callable<Integer> hotCallable = new Callable() {
            @Override
            public Object call() throws Exception {
                try {
                    String hotListStr = JsonPathUtils.jsonArray(spiderData, "$.hotList[*]");
                    if (StringUtils.isNotEmpty(hotListStr)) {
                        List<HotNews> hotNewsList = JSON.parseArray(hotListStr, HotNews.class);
                        hotInfoService.buildAndSave(hotNewsList, modules);
                    }
                    return 1;
                } catch (Exception e) {
                    logger.error("hotCallable error, msg:{}", e);
                    return 0;
                }
            }
        };

        Callable<Integer> newsCallable = new Callable() {
            @Override
            public Object call() throws Exception {
                try {
                    String lastestListStr = JsonPathUtils.jsonArray(spiderData, "$.lastestList[*]");
                    if (StringUtils.isNotEmpty(lastestListStr)) {
                        List<News> newsList = JSON.parseArray(lastestListStr, News.class);
                        newsInfoService.buildAneSaveList(newsList, modules, spiderWeb);
                    }
                    String historyListStr = JsonPathUtils.jsonArray(spiderData, "$.historyList[*]");
                    if (StringUtils.isNotEmpty(historyListStr)) {
                        List<News> newsList = JSON.parseArray(historyListStr, News.class);
                        newsInfoService.buildAneSaveList(newsList, modules, spiderWeb);
                    }
                    String topListStr = JsonPathUtils.jsonArray(spiderData, "$.topList[*]");
                    if (StringUtils.isNotEmpty(topListStr)) {
                        List<News> newsList = JSON.parseArray(topListStr, News.class);
                        newsInfoService.buildAneSaveList(newsList, modules, spiderWeb);
                    }
                    String recommendListStr = JsonPathUtils.jsonArray(spiderData, "$.recommendList[*]");
                    if (StringUtils.isNotEmpty(recommendListStr)) {
                        List<News> newsList = JSON.parseArray(recommendListStr, News.class);
                        newsInfoService.buildAneSaveList(newsList, modules, spiderWeb);
                    }

                    return 1;
                } catch (Exception e) {
                    logger.error("newsCallable is error:{}", e);
                    return 0;
                }
            }
        };
//
        Future<Integer> future1 = executorService.submit(hotCallable);
        Future<Integer> future2 = executorService.submit(newsCallable);
        Future<Integer> future3 = executorService.submit(statusCallable);
        if (future1.get() != 1) {
            logger.error("sotre data error");
            return false;
        }

        return true;
    }
}
