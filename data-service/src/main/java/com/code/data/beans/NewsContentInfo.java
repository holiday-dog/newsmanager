package com.code.data.beans;

public class NewsContentInfo {
    private Long id;

    private String newsSign;

    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNewsSign() {
        return newsSign;
    }

    public void setNewsSign(String newsSign) {
        this.newsSign = newsSign == null ? null : newsSign.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}