package com.code.web.hystrix;

import com.code.web.api.AnalyseServiceApi;
import org.springframework.stereotype.Component;

@Component
public class AnalyseServiceFallBack implements AnalyseServiceApi {
    @Override
    public String requsetAnalyseTest() {
        return "error!";
    }
}
