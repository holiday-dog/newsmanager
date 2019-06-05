package com.code.data.service;

import com.code.common.bean.News;
import com.code.common.enums.Modules;
import com.code.common.enums.NewsType;

import java.util.List;

public interface NewsInfoService {
    void buildAneSave(News news);

    void buildAneSaveList(List<News> newsList, Modules moduleType, String spiderWeb);

    News queryNews(String newsSign);

    List<News> queryList(Modules moduleType, NewsType newsType, Integer limit);

    List<News> queryList(Modules moduleType, NewsType newsType, Integer page, Integer limit);
}
