package com.code.web.service;

import com.alibaba.fastjson.JSON;
import com.code.common.bean.*;
import com.code.common.enums.NewsType;
import com.code.common.enums.ResultStatus;
import com.code.web.bean.NewsAdaptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/index")
public class TestControllerService {
    private static final String analyseService = "localhost:8084";
    private static final String dataService = "localhost:8081";


    @Autowired
    private RestTemplate remoteRestTemplate;

    @RequestMapping("/index")
    public ModelAndView index(@RequestParam(value = "msg", required = false) String msg) {
        String modules = "推荐";
        List<News> newsList = null;
        List<HotNews> hotNewsList = null;

        ModelAndView mv = new ModelAndView("modules");
        ResponseEntity<String> responseEntity = remoteRestTemplate.getForEntity("http://" + dataService + "/news/queryNewsList?modulesMsg=" + modules + "&newsMsg=" + NewsType.LATEST.getMsg() + "&limit=5", String.class);
        System.out.println(responseEntity.getBody());
        ResponseData responseData = JSON.parseObject(responseEntity.getBody(), ResponseData.class);
        newsList = JSON.parseArray((String) responseData.getResultData(), News.class);
        mv.addObject("newList", newsList);

        responseEntity = remoteRestTemplate.getForEntity("http://" + dataService + "/news/queryNewsList?modulesMsg=" + modules + "&newsMsg=" + NewsType.HISTORY.getMsg() + "&limit=2", String.class);
        System.out.println(responseEntity.getBody());
        responseData = JSON.parseObject(responseEntity.getBody(), ResponseData.class);
        List<News> hisList = JSON.parseArray((String) responseData.getResultData(), News.class);
        mv.addObject("historyList", hisList);

        responseEntity = remoteRestTemplate.getForEntity("http://" + dataService + "/news/queryNewsList?modulesMsg=" + modules + "&newsMsg=" + NewsType.TOP.getMsg() + "&limit=3", String.class);
        System.out.println(responseEntity.getBody());
        responseData = JSON.parseObject(responseEntity.getBody(), ResponseData.class);
        List<News> topList = JSON.parseArray((String) responseData.getResultData(), News.class);
        mv.addObject("topList", topList);

        responseEntity = remoteRestTemplate.getForEntity("http://" + dataService + "/hotnews/queryList?modules=" + modules + "&limit=3", String.class);
        System.out.println(responseEntity.getBody());
        responseData = JSON.parseObject(responseEntity.getBody(), ResponseData.class);
        hotNewsList = JSON.parseArray((String) responseData.getResultData(), HotNews.class);
        mv.addObject("hotList", hotNewsList);

        return mv;
    }

    @RequestMapping("/test")
    public ModelAndView test(@RequestParam(value = "referUrl", required = false) String referUrl) {
//        if (StringUtils.isNotEmpty(referUrl)) {
//            throw new CodecException("系统异常");
//        }
        return new ModelAndView("error").addObject("errorMsg", "<a href='" + referUrl + "'><font color='red'>读取页面失败，请进入原页面查看完整内容</font></a>");
    }


    @RequestMapping("/relations")
    public ModelAndView relations(@RequestParam(value = "referUrl", required = false) String referUrl, @RequestParam(value = "spiderWeb", required = false) String spiderWeb) {
        ModelAndView mv = new ModelAndView("relations");
        System.out.println(referUrl + ":" + spiderWeb);
        ResponseEntity<String> responseEntity = remoteRestTemplate.getForEntity("http://" + analyseService + "/analyse/searchcontent?referUrl=" + URLEncoder.encode(referUrl) + "&spiderWeb=" + spiderWeb, String.class);

        ResponseData responseData = JSON.parseObject(responseEntity.getBody(), ResponseData.class);
        if (!responseData.getResultCode().equals(ResultStatus.SUCCESS.getCode())) {
            return new ModelAndView("error").addObject("errorMsg", "<a href='" + referUrl + "'><font color='red'>读取页面失败，请进入原页面查看完整内容</font></a>");
        }
        NewsAdaptor news = JSON.parseObject((String) responseData.getResultData(), NewsAdaptor.class);
        if (StringUtils.isNotEmpty(news.getKeywords())) {
            news.setKeys(Arrays.asList(news.getKeywords().split("[,|\\s]")).stream().limit(5).toArray());
        }
//        System.out.println(JSON.toJSONString(news.getKeys()));
        List<News> relations = null;
        if (news.getKeys() != null) {
            int i = 0;
            while (CollectionUtils.isEmpty(relations) && i < news.getKeys().length - 1) {
                String keyword = (String) news.getKeys()[i];
                if (keyword.length() < 4) {
                    keyword = keyword + news.getKeys()[i + 1];
                }
                responseEntity = remoteRestTemplate.getForEntity("http://" + analyseService + "/analyse/relation?keyword=" + keyword, String.class);
                System.out.println(responseEntity.getBody());
                relations = JSON.parseArray((String) JSON.parseObject(responseEntity.getBody(), ResponseData.class).getResultData(), News.class);
                i++;
            }
            System.out.println(responseEntity.getBody());
            mv.addObject("relations", relations);
        }

        mv.addObject("news", news);
        return mv;
    }

    @RequestMapping("/content")
    public ModelAndView content(@RequestParam(value = "sign", required = false) String sign) {
        ModelAndView mv = new ModelAndView("content");
        ResponseEntity<String> responseEntity = remoteRestTemplate.getForEntity("http://" + dataService + "/news/queryNews?sign=" + sign, String.class);
        System.out.println(responseEntity.getBody());
        ResponseData responseData = JSON.parseObject(responseEntity.getBody(), ResponseData.class);
        NewsAdaptor news = JSON.parseObject((String) responseData.getResultData(), NewsAdaptor.class);
        String keywords = news.getKeywords();
        if (StringUtils.isEmpty(keywords)) {
            responseEntity = remoteRestTemplate.getForEntity("http://" + analyseService + "/analyse/pickup?sign=" + sign, String.class);
            List<Map.Entry> entryList = JSON.parseArray((String) JSON.parseObject(responseEntity.getBody(), ResponseData.class).getResultData(), Map.Entry.class);
//            System.out.println(JSON.toJSONString(entryList));
            String[] keys = new String[entryList.size()];
            for (int i = 0; i < entryList.size(); i++) {
                Map.Entry entry = entryList.get(i);
                keys[i] = entry.getKey().toString();
            }
            news.setKeys(keys);
        } else {
            news.setKeys(Arrays.asList(keywords.split("[,|\\s]")).stream().limit(5).toArray());
        }
        List<News> relations = null;
        if (news.getKeys() != null) {
            int i = 0;
            while (CollectionUtils.isEmpty(relations) && i < news.getKeys().length - 1) {
                String keyword = (String) news.getKeys()[i];
                if (keyword.length() < 4) {
                    keyword = keyword + news.getKeys()[i + 1];
                }
                responseEntity = remoteRestTemplate.getForEntity("http://" + analyseService + "/analyse/relation?keyword=" + keyword, String.class);
                relations = JSON.parseArray((String) JSON.parseObject(responseEntity.getBody(), ResponseData.class).getResultData(), News.class);
                i++;
            }
            System.out.println(responseEntity.getBody());
            mv.addObject("relations", relations);
        }

        mv.addObject("news", news);
        return mv;
    }

    @RequestMapping("/admin")
    public ModelAndView admin(@RequestParam(value = "page", required = false) String page, @RequestParam(value = "limit", required = false) String limit) {
        PageBean<ProcessInfo> pageBean = null;

        ModelAndView mv = new ModelAndView("admin");
        ResponseEntity<String> responseEntity = remoteRestTemplate.getForEntity("http://" + dataService + "/process/queryList?page=" + (page == null ? 0 : page) + "&limit=" + (limit == null ? 0 : limit), String.class);
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
        ResponseEntity<String> responseEntity = remoteRestTemplate.getForEntity("http://" + dataService + "/news/queryNewsList?modulesMsg=" + modules + "&newsMsg=" + NewsType.LATEST.getMsg() + "&limit=5", String.class);
        System.out.println(responseEntity.getBody());
        ResponseData responseData = JSON.parseObject(responseEntity.getBody(), ResponseData.class);
        newsList = JSON.parseArray((String) responseData.getResultData(), News.class);
        mv.addObject("newList", newsList);

        responseEntity = remoteRestTemplate.getForEntity("http://" + dataService + "/news/queryNewsList?modulesMsg=" + modules + "&newsMsg=" + NewsType.HISTORY.getMsg() + "&limit=3", String.class);
        System.out.println(responseEntity.getBody());
        responseData = JSON.parseObject(responseEntity.getBody(), ResponseData.class);
        List<News> hisList = JSON.parseArray((String) responseData.getResultData(), News.class);
        mv.addObject("historyList", hisList);

        responseEntity = remoteRestTemplate.getForEntity("http://" + dataService + "/news/queryNewsList?modulesMsg=" + modules + "&newsMsg=" + NewsType.TOP.getMsg() + "&limit=3", String.class);
        System.out.println(responseEntity.getBody());
        responseData = JSON.parseObject(responseEntity.getBody(), ResponseData.class);
        List<News> topList = JSON.parseArray((String) responseData.getResultData(), News.class);
        mv.addObject("topList", topList);


        responseEntity = remoteRestTemplate.getForEntity("http://" + dataService + "/hotnews/queryList?modules=" + modules + "&limit=5", String.class);
        System.out.println(responseEntity.getBody());
        responseData = JSON.parseObject(responseEntity.getBody(), ResponseData.class);
        hotNewsList = JSON.parseArray((String) responseData.getResultData(), HotNews.class);
        mv.addObject("hotList", hotNewsList);

        return mv;
    }

    @RequestMapping("/search")
    public ModelAndView search(@RequestParam("keyword") String keyword) {
        List<News> newsList = new ArrayList<>();
        ModelAndView mv = new ModelAndView("search");
        ResponseEntity<String> responseEntity = remoteRestTemplate.getForEntity("http://" + analyseService + "/analyse/search?keyword=" + keyword, String.class);
        System.out.println(responseEntity.getBody());
        newsList = JSON.parseArray((String) JSON.parseObject(responseEntity.getBody(), ResponseData.class).getResultData(), News.class);
//        newsList = JsonPathUtils.getObjList(responseEntity.getBody(), "$.resultData[*]", News.class);

        mv.addObject("searchList", newsList);
        return mv;
    }
}
