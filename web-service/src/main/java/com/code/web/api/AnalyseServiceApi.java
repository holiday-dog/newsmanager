package com.code.web.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@FeignClient(value = "ANALYSE-SERVICE")
public interface AnalyseServiceApi {
    @RequestMapping(value = "/analyse/test")
    String requsetAnalyseTest();
}
