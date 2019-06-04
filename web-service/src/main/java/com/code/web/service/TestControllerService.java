package com.code.web.service;

import com.alibaba.fastjson.JSON;
import com.code.common.bean.*;
import com.code.common.enums.NewsType;
import com.code.common.utils.JsonPathUtils;
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

//    @RequestMapping("/modules")
//    public String modules(@RequestParam(value = "msg", required = false) String msg) {
//        if (StringUtils.isNotEmpty(msg)) {
//            throw new CodecException("系统异常");
//        }
//        return "modules";
//    }

    @RequestMapping("/content")
    public String content(@RequestParam(value = "msg", required = false) String msg) {
        if (StringUtils.isNotEmpty(msg)) {
            throw new CodecException("系统异常");
        }
        return "content";
    }

    @RequestMapping("/admin")
    public ModelAndView admin(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "limit", required = false) String limit) {
        PageBean<ProcessInfo> pageBean = null;

        ModelAndView mv = new ModelAndView("admin");
        ResponseEntity<String> responseEntity = remoteRestTemplate.getForEntity("http://localhost:8081/process/queryList?page=" + (page == null ? 0 : page) + "&limit=" + (limit == null ? 0 : limit), String.class);
        System.out.println(responseEntity.getBody());
        ResponseData responseData = JSON.parseObject(responseEntity.getBody(), ResponseData.class);
        pageBean = JSON.parseObject((String) responseData.getResultData(), PageBean.class);
        System.out.println(JSON.toJSONString(pageBean));
        mv.addObject("processPageBean", pageBean);

        return mv;
    }

    @RequestMapping("/modules")
    public ModelAndView queryNewsList(@RequestParam("modules") String modules) {
        List<News> newsList = null;
        List<HotNews> hotNewsList = null;

        ModelAndView mv = new ModelAndView("modules");
        ResponseEntity<String> responseEntity = remoteRestTemplate.getForEntity("http://localhost:8081/news/queryNewsList?modulesMsg=" + modules + "&newsMsg=" + NewsType.LATEST.getMsg() + "&limit=5", String.class);
        System.out.println(responseEntity.getBody());
        ResponseData responseData = JSON.parseObject(responseEntity.getBody(), ResponseData.class);
        newsList = JSON.parseArray((String) responseData.getResultData(), News.class);
        System.out.println(JSON.toJSONString(newsList));
        mv.addObject("newList", newsList);

        responseEntity = remoteRestTemplate.getForEntity("http://localhost:8081/news/queryNewsList?modulesMsg=" + modules + "&newsMsg=" + NewsType.HISTORY.getMsg() + "&limit=2", String.class);
        System.out.println(responseEntity.getBody());
        responseData = JSON.parseObject(responseEntity.getBody(), ResponseData.class);
        List<News> hisList = JSON.parseArray((String) responseData.getResultData(), News.class);
        System.out.println(JSON.toJSONString(hisList));
        mv.addObject("historyList", hisList);


        responseEntity = remoteRestTemplate.getForEntity("http://localhost:8081/hotnews/queryList?modules=" + modules + "&limit=3", String.class);
        System.out.println(responseEntity.getBody());
        responseData = JSON.parseObject(responseEntity.getBody(), ResponseData.class);
        hotNewsList = JSON.parseArray((String) responseData.getResultData(), HotNews.class);
        System.out.println(JSON.toJSONString(hotNewsList));
        mv.addObject("hotList", hotNewsList);

        return mv;
    }

    @RequestMapping("/search")
    public ModelAndView search(@RequestParam("keyword") String keyword) {
        List<News> newsList = new ArrayList<>();
        ResponseEntity<String> responseEntity = remoteRestTemplate.getForEntity("http://localhost:8084/analyse/search?keyword=" + keyword, String.class);

        newsList = JsonPathUtils.getObjList(responseEntity.getBody(), "$.resultData[*]", News.class);
        System.out.println(JSON.toJSONString(newsList));

        return new ModelAndView("search", "searchList", newsList);
    }
}
