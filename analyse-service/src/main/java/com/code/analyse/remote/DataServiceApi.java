package com.code.analyse.remote;

import com.alibaba.fastjson.JSON;
import com.code.common.bean.News;
import com.code.common.bean.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DataServiceApi {
    @Autowired
    private RestTemplate remoteRestTemplate;

    public News queryContentInfoBySign(String sign) {
        ResponseEntity<String> responseEntity = remoteRestTemplate.getForEntity("http://localhost:8081/news/queryNews?sign=" + sign, String.class);
        ResponseData responseData = JSON.parseObject(responseEntity.getBody(), ResponseData.class);
        News news = JSON.parseObject((String) responseData.getResultData(), News.class);

        return news;
    }

}
