package com.code.data.serviceimpl;

import com.code.common.bean.HotNews;
import com.code.common.enums.Modules;
import com.code.data.beans.NewsContentInfo;
import com.code.data.beans.NewsHotInfo;
import com.code.data.dao.NewsHotInfoMapper;
import com.code.data.service.NewsHotInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class NewsHotInfoServiceImpl implements NewsHotInfoService {
    @Resource
    private NewsHotInfoMapper newsHotInfoMapper;
    private Logger logger = LoggerFactory.getLogger(NewsHotInfoServiceImpl.class);

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 10000)
    public void buildAndSave(List<HotNews> hotInfoList, Modules modules) {
        if (CollectionUtils.isEmpty(hotInfoList)) {
            logger.info("newsList is empty");
            return;
        }
        List<NewsHotInfo> newsInfoList = new ArrayList<>();
        List<NewsContentInfo> newsContentInfoList = new ArrayList<>();
        for (HotNews hotNews : hotInfoList) {
            NewsHotInfo newsHotInfo = new NewsHotInfo();
            BeanUtils.copyProperties(hotNews, newsHotInfo);
            newsHotInfo.setModulesType(modules.getValue());
            newsInfoList.add(newsHotInfo);
        }
        if (!CollectionUtils.isEmpty(newsInfoList)) {
            newsHotInfoMapper.insertList(newsInfoList);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public HotNews queryHotNew(String id) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<HotNews> queryHotNewsList(Modules modules, Integer limit) {
        if (limit == null) {
            limit = 5;
        }
        List<NewsHotInfo> newsHotInfoList = newsHotInfoMapper.selectListBylimit(modules.getValue(), limit);

        List<HotNews> hotNewsList = null;
        if (!CollectionUtils.isEmpty(newsHotInfoList)) {
            hotNewsList = new ArrayList<>();
            for (NewsHotInfo newsInfo : newsHotInfoList) {
                HotNews news = new HotNews();
                BeanUtils.copyProperties(newsInfo, news);
                hotNewsList.add(news);
            }
        }
        return hotNewsList;
    }
}
