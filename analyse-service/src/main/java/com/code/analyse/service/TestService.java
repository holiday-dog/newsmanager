package com.code.analyse.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/analyse")
public class TestService {

    @RequestMapping(value = "/test")
    public String test() {
        return "analyse";
    }
}
