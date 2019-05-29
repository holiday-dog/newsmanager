package com.code.data.beans;

import java.util.Date;

public class NewsHotInfo {
    private Long id;

    private String title;

    private String images;

    private String referurl;

    private Date createdate;

    private Date lastupdatedate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images == null ? null : images.trim();
    }

    public String getReferurl() {
        return referurl;
    }

    public void setReferurl(String referurl) {
        this.referurl = referurl == null ? null : referurl.trim();
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Date getLastupdatedate() {
        return lastupdatedate;
    }

    public void setLastupdatedate(Date lastupdatedate) {
        this.lastupdatedate = lastupdatedate;
    }
}