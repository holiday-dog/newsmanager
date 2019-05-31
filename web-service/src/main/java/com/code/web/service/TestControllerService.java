package com.code.web.service;

import com.alibaba.fastjson.JSON;
import com.code.common.bean.News;
import com.code.common.bean.ResponseData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.codec.CodecException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/index")
public class TestControllerService {
    @Autowired
    private RestTemplate remoteRestTemplate;

    @RequestMapping("/index")
    public String index(@RequestParam(value = "msg", required = false) String msg) {
        if (StringUtils.isNotEmpty(msg)) {
            throw new CodecException("系统异常");
        }
        return "index";
    }

    @RequestMapping("/test")
    public String test(@RequestParam(value = "msg", required = false) String msg) {
        if (StringUtils.isNotEmpty(msg)) {
            throw new CodecException("系统异常");
        }
        return "test";
    }

    @RequestMapping("/newList")
    public ModelAndView queryNewsList(@RequestParam("modules") String modules, @RequestParam("type") String type) {

        List<News> newsList = new ArrayList<>();
        ResponseEntity<String> responseEntity = remoteRestTemplate.getForEntity("http://localhost:8081/news/queryNewsList?modulesMsg=" + modules + "&newsMsg=" + type, String.class);

        ResponseData responseData = JSON.parseObject(responseEntity.getBody(), ResponseData.class);
        newsList = JSON.parseArray((String) responseData.getResultData(), News.class);
        System.out.println(JSON.toJSONString(newsList));

        return new ModelAndView("index", "newList", newsList);
    }

}
