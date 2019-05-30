package com.code.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableEurekaClient
@EnableTransactionManagement
//数据库服务
public class DataApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataApplication.class, args);
    }
}
