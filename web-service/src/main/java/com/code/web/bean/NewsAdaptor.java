package com.code.web.bean;

import com.code.common.enums.Modules;
import com.code.common.enums.NewsType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewsAdaptor {
    private String title;

    private String sign;

    private String description;

    private String keywords;

    private String author;

    private Modules moduleType;

    private String content;

    private LocalDateTime pubTime;

    private String spiderWeb;

    private String articleSource;

    private String referUrl;

    private String images;

    private NewsType newsType;

    private Object[] keys;

}
