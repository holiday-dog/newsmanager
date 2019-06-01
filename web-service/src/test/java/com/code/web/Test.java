package com.code.web;

import com.code.common.utils.DateUtils;

import java.time.LocalDateTime;

public class Test {
    @org.junit.Test
    public void test11(){
        System.out.println(DateUtils.formatDateTime(LocalDateTime.now()));
    }
}
