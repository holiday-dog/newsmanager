package com.code.data.controller;

import com.alibaba.fastjson.JSON;
import com.code.common.bean.ResponseData;
import com.code.common.enums.ResultStatus;
import com.code.common.utils.ResponseUtils;
import com.code.data.beans.ProcessInfo;
import com.code.data.service.ProcessInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/process")
public class ProcessInfoController {
    @Autowired
    private ProcessInfoService processInfoService;

    @ResponseBody
    @RequestMapping("/queryList")
    public String queryList(@RequestParam("page") Integer page, @RequestParam("limit") int limit) {
        ResponseData responseData = new ResponseData();
        try {
            List<ProcessInfo> processInfoList = processInfoService.selectListOrderSpiderTimeByPage(page, limit);
            responseData.setResultData(JSON.toJSONString(processInfoList));
            ResponseUtils.setStatus(responseData, ResultStatus.SUCCESS);
        } catch (Exception e) {
            ResponseUtils.setStatus(responseData, ResultStatus.SYSTEM_ERROR);
        }
        return responseData.toString();
    }
}
