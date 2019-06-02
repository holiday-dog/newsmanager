package com.code.data.controller;

import com.alibaba.fastjson.JSON;
import com.code.common.bean.HotNews;
import com.code.common.bean.ResponseData;
import com.code.common.enums.Modules;
import com.code.common.enums.ResultStatus;
import com.code.common.utils.ResponseUtils;
import com.code.data.service.NewsHotInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hotnews")
public class HotNewsController {
    @Autowired
    private NewsHotInfoService hotInfoService;

    @ResponseBody
    @RequestMapping("/queryList")
    public String queryList(@RequestParam("modules") String modules, @RequestParam(required = false) int limit) {
        ResponseData responseData = new ResponseData();
        try {
            List<HotNews> hotNewsList = hotInfoService.queryHotNewsList(Modules.parse(modules), limit);
            responseData.setResultData(JSON.toJSONString(hotNewsList));
            ResponseUtils.setStatus(responseData, ResultStatus.SUCCESS);
        } catch (Exception e) {
            ResponseUtils.setStatus(responseData, ResultStatus.SYSTEM_ERROR);
        }
        return responseData.toString();
    }
}
