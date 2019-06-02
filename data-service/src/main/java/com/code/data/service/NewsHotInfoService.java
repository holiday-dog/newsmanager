package com.code.data.service;

import com.code.common.bean.HotNews;
import com.code.common.enums.Modules;
import com.code.data.beans.NewsHotInfo;

import java.util.List;

public interface NewsHotInfoService {
    public void buildAndSave(List<HotNews> hotInfoList, Modules modules);

    HotNews queryHotNew(String id);

    List<HotNews> queryHotNewsList();
}
