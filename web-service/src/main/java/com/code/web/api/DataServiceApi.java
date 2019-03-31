package com.code.web.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@FeignClient(value = "DATA-SERVICE")
public interface DataServiceApi {

    @RequestMapping(value = "/test/test1")
    String requestTestTest1();
}
