package com.code.common.bean;

import com.code.common.enums.Modules;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class News {
    private String title;

    private String description;

    private String keywords;

    private String author;

    private Modules moduleType;

    private String content;

    private LocalDateTime pubTime;

    private String spiderWeb;

    private String articleSource;

    private String referUrl;

    private List<String> relationUrls;

    private List<String> relationTitles;

    public News(String title, String description, String keywords, String author, String content, LocalDateTime pubTime, String articleSource, String referUrl) {
        this.title = title;
        this.description = description;
        this.keywords = keywords;
        this.author = author;
        this.content = content;
        this.pubTime = pubTime;
        this.articleSource = articleSource;
        this.referUrl = referUrl;
    }

    public News(String title, String description, String keywords, String author, Modules moduleType, String content, LocalDateTime pubTime, String spiderWeb, String articleSource) {
        this.title = title;
        this.description = description;
        this.keywords = keywords;
        this.author = author;
        this.moduleType = moduleType;
        this.content = content;
        this.pubTime = pubTime;
        this.spiderWeb = spiderWeb;
        this.articleSource = articleSource;
    }
}
