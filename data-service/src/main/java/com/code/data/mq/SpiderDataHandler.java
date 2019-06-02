package com.code.data.mq;

import com.alibaba.fastjson.JSON;
import com.code.common.bean.HotNews;
import com.code.common.bean.News;
import com.code.common.enums.Modules;
import com.code.common.utils.JsonPathUtils;
import com.code.data.service.NewsHotInfoService;
import com.code.data.service.NewsInfoService;
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
    private ExecutorService executorService = Executors.newFixedThreadPool(2);
    private Logger logger = LoggerFactory.getLogger(SpiderDataHandler.class);

    public boolean handler(String spiderData) throws ExecutionException, InterruptedException {

        Callable<Integer> hotCallable = new Callable() {
            @Override
            public Object call() throws Exception {
                String hotListStr = JsonPathUtils.jsonArray(spiderData, "$.hotList[*]");
                if (StringUtils.isNotEmpty(hotListStr)) {
                    List<HotNews> hotNewsList = JSON.parseArray(hotListStr, HotNews.class);
                    hotInfoService.buildAndSave(hotNewsList, Modules.EDUCATION);
                }
                return 1;
            }
        };
        Callable<Integer> newCallable = new Callable() {
            @Override
            public Object call() throws Exception {
                String lastestListStr = JsonPathUtils.jsonArray(spiderData, "$.lastestList[*]");
                if (StringUtils.isNotEmpty(lastestListStr)) {
                    List<News> newsList = JSON.parseArray(lastestListStr, News.class);
                    newsInfoService.buildAneSaveList(newsList, Modules.EDUCATION, "Xinhua");
                }
                String historyListStr = JsonPathUtils.jsonArray(spiderData, "$.historyList[*]");
                if (StringUtils.isNotEmpty(historyListStr)) {
                    List<News> newsList = JSON.parseArray(historyListStr, News.class);
                    newsInfoService.buildAneSaveList(newsList, Modules.EDUCATION, "Xinhua");
                }

                return 1;
            }
        };

        Future<Integer> future1 = executorService.submit(hotCallable);
        Future<Integer> future2 = executorService.submit(newCallable);
        if (future1.get() != 1 || future2.get() != 1) {
            logger.error("happend error");
        }

        return false;
    }
}
