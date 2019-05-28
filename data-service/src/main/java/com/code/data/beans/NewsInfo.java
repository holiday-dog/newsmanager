package com.code.data.beans;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewsInfo {
    private Long id;

    private String title;

    private String description;

    private String keywords;

    private String author;

    private Byte modulesType;

    private String images;

    private String content;

    private LocalDateTime pubTime;

    private String spiderWeb;

    private String articleSource;

    private String referUrl;
}
