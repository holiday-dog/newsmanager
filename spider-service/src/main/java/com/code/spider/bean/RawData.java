package com.code.spider.bean;

import com.code.common.enums.NewsType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawData {
    private String url;

    private String page;

    private NewsType newsType;

    private String others;

    public RawData(String url, String page) {
        this.url = url;
        this.page = page;
    }

    public RawData(String url, String page, NewsType newsType) {
        this.url = url;
        this.page = page;
        this.newsType = newsType;
    }
}
