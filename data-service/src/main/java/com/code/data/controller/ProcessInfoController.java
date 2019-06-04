package com.code.data.controller;

import com.alibaba.fastjson.JSON;
import com.code.common.bean.PageBean;
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
    public String queryList(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "limit", required = false) Integer limit) {
        ResponseData responseData = new ResponseData();
        try {
            PageBean<ProcessInfo> pageBean = new PageBean<>();
            page = (page == null || page == 0) ? 1 : page;
            limit = (limit == null || limit == 0) ? 10 : limit;


            pageBean.setCurrentPage(page);
            List<ProcessInfo> processInfoList = processInfoService.selectListOrderSpiderTimeByPage(page, limit);
            pageBean.setInfoList(processInfoList);
            Long totalCount = processInfoService.getTotalPage();
            pageBean.setTotalPage(totalCount % limit == 0 ? totalCount / limit : (totalCount / limit + 1));
            responseData.setResultData(JSON.toJSONString(pageBean));
            ResponseUtils.setStatus(responseData, ResultStatus.SUCCESS);
        } catch (Exception e) {
            ResponseUtils.setStatus(responseData, ResultStatus.SYSTEM_ERROR);
        }
        return responseData.toString();
    }
}
