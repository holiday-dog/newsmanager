package com.code.analyse.remote;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Conf {
    @Bean(name = "remoteRestTemplate")
    public RestTemplate remoteRestTemplate() {
        return new RestTemplate();
    }
}
