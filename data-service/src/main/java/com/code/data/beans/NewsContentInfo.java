package com.code.data.beans;

public class NewsContentInfo {
    private Long id;

    private Long newid;

    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNewid() {
        return newid;
    }

    public void setNewid(Long newid) {
        this.newid = newid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}