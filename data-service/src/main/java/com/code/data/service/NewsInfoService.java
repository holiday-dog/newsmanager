package com.code.data.service;

import com.code.common.bean.News;

import java.util.List;

public interface NewsInfoService {
    void buildAneSave(News news);

    News queryNews();


    List<News> queryList();
}
