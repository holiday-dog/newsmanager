package com.code.data.beans;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class NewsInfo {
    private Long id;

    private String title;

    private String sign;

    private String keywords;

    private String description;

    private String author;

    private Byte modulesType;

    private String images;

    private LocalDateTime pubTime;

    private String spiderWeb;

    private String articleSource;

    private String referUrl;

    private Byte newsType;

    private Date createDate;

    private Date lastUpdateDate;

}