package com.code.data.service;

import com.code.common.bean.HotNews;

import java.util.List;

public interface NewHotInfoService {
    public void buildAndSave(HotNews hotNews);

    HotNews queryHotNew(String id);

    List<HotNews> queryHotNewsList();
}
