package com.code.center.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
//@RequestMapping(value = "/center")
public class TestController {
//    @RequestMapping(value = "/test")
    public String test() {
        return "success";
    }
}
