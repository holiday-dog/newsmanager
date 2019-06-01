package com.code.web.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.time.LocalDateTime;


@Configuration
public class SpringWebConfig extends WebMvcConfigurationSupport {

    @Override
    public void addFormatters(FormatterRegistry registry) {
//        registry.addFormatterForFieldAnnotation(new DateTimeFormatAnnotationFormatterFactory());
        super.addFormatters(registry);
        registry.addFormatterForFieldType(LocalDateTime.class, dateTimeFormatter());
    }

    @Bean
    public LocalDateTimeFormatter dateTimeFormatter() {
        return new LocalDateTimeFormatter();
    }
}