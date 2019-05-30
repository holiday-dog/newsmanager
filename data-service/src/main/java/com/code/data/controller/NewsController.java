package com.code.data.controller;

import com.alibaba.fastjson.JSON;
import com.code.common.bean.News;
import com.code.common.bean.ResponseData;
import com.code.common.enums.Modules;
import com.code.common.enums.NewsType;
import com.code.data.service.NewsInfoService;
import com.code.data.utils.ResponseUtils;
import com.code.data.utils.ResultStatus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {
    @Autowired
    private NewsInfoService newsInfoService;
    private Logger logger = LoggerFactory.getLogger(NewsController.class);

    @RequestMapping("/queryNewsList")
    public String queryNewsList(@RequestParam("modulesMsg") String modulesMsg, @RequestParam("newsMsg") String newsMsg) {
        ResponseData responseData = new ResponseData();

        logger.info("msg:{}, {}", modulesMsg, newsMsg);
        try {
            if (StringUtils.isEmpty(modulesMsg) || StringUtils.isEmpty(newsMsg)) {
                logger.error("param is not enough, modulesMsg:{}, newsMsg:{}", modulesMsg, newsMsg);
                ResponseUtils.setStatus(responseData, ResultStatus.NOT_ENOUGH_PARAMS);
                return responseData.toString();
            }
            List<News> newsList = newsInfoService.queryList(Modules.parse(modulesMsg), NewsType.parse(newsMsg));
            ResponseUtils.setStatus(responseData, ResultStatus.SUCCESS);
            responseData.setResultData(JSON.toJSONString(newsList));
        } catch (Exception e) {
            logger.error("request error:{}", e);
            ResponseUtils.setStatus(responseData, ResultStatus.SYSTEM_ERROR);
        }
        return responseData.toString();
    }

    @RequestMapping("/queryNews")
    public String queryNewsList(@RequestParam("sign") String newsSign) {
        ResponseData responseData = new ResponseData();

        logger.info("msg:{}, {}", newsSign);
        try {
            if (StringUtils.isEmpty(newsSign)) {
                logger.error("param is not enough, newssign:{}", newsSign);
                ResponseUtils.setStatus(responseData, ResultStatus.NOT_ENOUGH_PARAMS);
                return responseData.toString();
            }
            News news = newsInfoService.queryNews(newsSign);
            ResponseUtils.setStatus(responseData, ResultStatus.SUCCESS);
            responseData.setResultData(JSON.toJSONString(news));
        } catch (Exception e) {
            logger.error("request error:{}", e);
            ResponseUtils.setStatus(responseData, ResultStatus.SYSTEM_ERROR);
        }
        return responseData.toString();
    }
}
