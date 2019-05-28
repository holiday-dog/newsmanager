package com.code.spider.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RawData {
    private String url;

    private String page;

    private String others;

    public RawData(String url, String page) {
        this.url = url;
        this.page = page;
    }
}
