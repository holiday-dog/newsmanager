package com.code.analyse.service;

import com.code.analyse.handler.KeyWordExtractor;
import com.code.analyse.handler.SearchExtractor;
import com.code.common.bean.News;
import com.code.common.bean.ResponseData;
import com.code.common.enums.ResultStatus;
import com.code.common.utils.ResponseUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/analyse")
public class AnalyseService {
    @RequestMapping(value = "search")
    public String keyword(@RequestParam("keyword") String keyWord) {
        SearchExtractor searchExtractor = new SearchExtractor();
        ResponseData responseData = new ResponseData();
        try {
            List<News> newsList = searchExtractor.searchKeyWord(keyWord);
            responseData.setResultData(newsList);
            ResponseUtils.setStatus(responseData, ResultStatus.SUCCESS);
        } catch (IOException e) {
            ResponseUtils.setStatus(responseData, ResultStatus.SUCCESS);
        }
        return responseData.toString();
    }

    @RequestMapping(value = "pickup")
    public String pickup(@RequestParam("content") String content) {
        KeyWordExtractor keyWordExtractor = new KeyWordExtractor();
        ResponseData responseData = new ResponseData();
        try {
            List keyWordList = keyWordExtractor.analyse(content);
            responseData.setResultData(keyWordList);
            ResponseUtils.setStatus(responseData, ResultStatus.SUCCESS);
        } catch (Exception e) {
            ResponseUtils.setStatus(responseData, ResultStatus.SUCCESS);
        }
        return responseData.toString();
    }
}
