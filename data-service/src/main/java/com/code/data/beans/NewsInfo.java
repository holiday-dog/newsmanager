package com.code.data.beans;

import java.util.Date;

public class NewsInfo {
    private Integer id;

    private String title;

    private String description;

    private String author;

    private Byte modulestype;

    private String images;

    private Date pubtime;

    private String spiderweb;

    private String articlesource;

    private String referurl;

    private Date createdate;

    private Date lastupdatedate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    public Byte getModulestype() {
        return modulestype;
    }

    public void setModulestype(Byte modulestype) {
        this.modulestype = modulestype;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images == null ? null : images.trim();
    }

    public Date getPubtime() {
        return pubtime;
    }

    public void setPubtime(Date pubtime) {
        this.pubtime = pubtime;
    }

    public String getSpiderweb() {
        return spiderweb;
    }

    public void setSpiderweb(String spiderweb) {
        this.spiderweb = spiderweb == null ? null : spiderweb.trim();
    }

    public String getArticlesource() {
        return articlesource;
    }

    public void setArticlesource(String articlesource) {
        this.articlesource = articlesource == null ? null : articlesource.trim();
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