package com.code.spider.bean;

import com.code.common.enums.Modules;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SpiderData {
    private String title;

    private String author;

    private Modules moduleType;

    private List<String> images;

    private String content;

    private Date pubTime;

    private String source;

    private String referUrl;

    private List<String> relationUrls;
}
