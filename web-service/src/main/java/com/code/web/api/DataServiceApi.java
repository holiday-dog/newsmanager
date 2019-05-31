package com.code.web.api;

import com.code.web.hystrix.DataServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@FeignClient(value = "DATA-SERVICE", fallback = DataServiceFallBack.class)
public interface DataServiceApi {

    @RequestMapping(value = "/test/test1")
    String requestTestTest1();

}