package com.code.web.hystrix;

import com.code.web.api.DataServiceApi;
import org.springframework.stereotype.Component;

@Component
public class DataServiceFallBack implements DataServiceApi {
    @Override
    public String requestTestTest1() {
        return "data-service error";
    }
}
