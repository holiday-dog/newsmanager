package com.code.analyse.service;

import com.alibaba.fastjson.JSON;
import com.code.analyse.handler.KeyWordExtractor;
import com.code.analyse.handler.SearchExtractor;
import com.code.analyse.remote.DataServiceApi;
import com.code.analyse.utils.ConvertUtils;
import com.code.common.bean.News;
import com.code.common.bean.ResponseData;
import com.code.common.enums.ResultStatus;
import com.code.common.utils.ResponseUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

@RestController
@RequestMapping("/analyse")
public class AnalyseService {
    @Autowired
    private DataServiceApi dataServiceApi;

    @RequestMapping(value = "search")
    public String keyword(@RequestParam("keyword") String keyWord) {
        SearchExtractor searchExtractor = new SearchExtractor();
        ResponseData responseData = new ResponseData();
        try {
            List<News> newsList = searchExtractor.searchKeyWord(keyWord);
            responseData.setResultData(JSON.toJSONString(newsList));
            ResponseUtils.setStatus(responseData, ResultStatus.SUCCESS);
        } catch (IOException e) {
            ResponseUtils.setStatus(responseData, ResultStatus.SYSTEM_ERROR);
        }
        return responseData.toString();
    }

    @RequestMapping(value = "pickup")
    public String pickup(@RequestParam("sign") String sign) {
        KeyWordExtractor keyWordExtractor = new KeyWordExtractor();
        ResponseData responseData = new ResponseData();
        try {
            String content = null;
            content = dataServiceApi.queryContentInfoBySign(sign).getContent();
            List keyWordList = keyWordExtractor.analyse(content);
            responseData.setResultData(JSON.toJSONString(keyWordList));
            ResponseUtils.setStatus(responseData, ResultStatus.SUCCESS);
        } catch (Exception e) {
            ResponseUtils.setStatus(responseData, ResultStatus.SYSTEM_ERROR);
        }
        return responseData.toString();
    }

    @RequestMapping(value = "pickupcontent")//
    public String pickupContent(@RequestParam("content") String content) {
        KeyWordExtractor keyWordExtractor = new KeyWordExtractor();
        ResponseData responseData = new ResponseData();
        try {
            List keyWordList = keyWordExtractor.analyse(content);
            responseData.setResultData(JSON.toJSONString(keyWordList));
            ResponseUtils.setStatus(responseData, ResultStatus.SUCCESS);
        } catch (Exception e) {
            ResponseUtils.setStatus(responseData, ResultStatus.SYSTEM_ERROR);
        }
        return responseData.toString();
    }

    @RequestMapping(value = "searchcontent")
    public String searchcontent(@RequestParam("referUrl") String referUrl, @RequestParam("spiderWeb") String spiderWeb) {
        SearchExtractor searchExtractor = new SearchExtractor();
        ResponseData responseData = new ResponseData();
        try {
            News news = searchExtractor.searchContent(URLDecoder.decode(referUrl), spiderWeb);
            if (StringUtils.isEmpty(news.getKeywords())) {
                news.setKeywords(ConvertUtils.convertMapList(new KeyWordExtractor().analyse(news.getContent()), ","));
            }
            responseData.setResultData(JSON.toJSONString(news));
            ResponseUtils.setStatus(responseData, ResultStatus.SUCCESS);
        } catch (Exception e) {
            ResponseUtils.setStatus(responseData, ResultStatus.SYSTEM_ERROR);
        }
        return responseData.toString();
    }

    @RequestMapping(value = "relation")
    public String relations(@RequestParam("keyword") String keyWord) {
        SearchExtractor searchExtractor = new SearchExtractor();
        ResponseData responseData = new ResponseData();
        try {
            List<News> newsList = searchExtractor.relationKeyWord(keyWord);
            responseData.setResultData(JSON.toJSONString(newsList));
            ResponseUtils.setStatus(responseData, ResultStatus.SUCCESS);
        } catch (IOException e) {
            ResponseUtils.setStatus(responseData, ResultStatus.SYSTEM_ERROR);
        }
        return responseData.toString();
    }
}
