package com.code.web.service;

import com.code.web.api.AnalyseServiceApi;
import com.code.web.api.DataServiceApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "/test")
public class TestService {
    private Logger logger = LoggerFactory.getLogger(TestService.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private DataServiceApi dataServiceApi;
    @Autowired
    private AnalyseServiceApi analyseServiceApi;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String test() {
        logger.info("请求data-service服务..");
        ResponseEntity<String> json = restTemplate.getForEntity("http://DATA-SERVICE/test/test1", String.class);
        return json.getBody();
    }
    @RequestMapping(value = "/hello1", method = RequestMethod.GET)
    public String test1() {
        logger.info("请求data-service服务..");
        return dataServiceApi.requestTestTest1();
    }
    @RequestMapping(value = "/analyse", method = RequestMethod.GET)
    public String test3() {
        logger.info("请求analyse-service服务..");
        return analyseServiceApi.requsetAnalyseTest();
    }
}
