package com.code.data.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hotnews")
public class HotNewsController {
    @ResponseBody
    @RequestMapping("/queryList")
    public String queryList() {

        return StringUtils.EMPTY;
    }
}
