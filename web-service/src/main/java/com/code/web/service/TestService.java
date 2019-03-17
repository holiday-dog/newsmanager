package com.code.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test")
public class TestService {
    private Logger logger = LoggerFactory.getLogger(TestService.class);

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String test() {
        logger.info("请求test方法..");
        return "hello";
    }
}
