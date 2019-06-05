package com.code.data.serviceimpl;

import com.code.common.bean.News;
import com.code.common.enums.Modules;
import com.code.common.enums.NewsType;
import com.code.data.beans.NewsContentInfo;
import com.code.data.beans.NewsInfo;
import com.code.data.dao.NewsContentInfoMapper;
import com.code.data.dao.NewsInfoMapper;
import com.code.data.service.NewsInfoService;
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

/**
 * 新闻信息服务类，提供对统一对象转换为javabean对象，将数据服务添加到msql数据库中
 * 对外提供新闻信息查询服务
 */
@Service
public class NewsInfoServiceImpl implements NewsInfoService {
    @Resource
    private NewsInfoMapper newsInfoMapper;
    @Resource
    private NewsContentInfoMapper newsContentInfoMapper;
    private Logger logger = LoggerFactory.getLogger(NewsInfoServiceImpl.class);

    @Override
    //添加事务支持
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 10000)
    public void buildAneSave(News news) {

    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 10000)
    public void buildAneSaveList(List<News> newsList, Modules moduleType, String spiderWeb) {
        if (CollectionUtils.isEmpty(newsList)) {
            logger.info("newsList is empty");
            return;
        }
        List<NewsInfo> newsInfoList = new ArrayList<>();
        List<NewsContentInfo> newsContentInfoList = new ArrayList<>();
        //转换新闻信息类
        for (News news : newsList) {
            NewsInfo newsInfo = new NewsInfo();
            BeanUtils.copyProperties(news, newsInfo);
            newsInfo.setSpiderWeb(spiderWeb);
            newsInfo.setModulesType(moduleType.getValue());
            newsInfo.setNewsType(news.getNewsType().getVal());
            newsInfoList.add(newsInfo);
        }
        if (!CollectionUtils.isEmpty(newsInfoList)) {
            newsInfoMapper.insertList(newsInfoList);
        }
        //转换新闻内容类，通过sign进行映射
        for (int i = 0; i < newsInfoList.size(); i++) {
            NewsContentInfo newsContentInfo = new NewsContentInfo();
            newsContentInfo.setNewsSign(newsInfoList.get(i).getSign());
            newsContentInfo.setContent(newsList.get(i).getContent());

            newsContentInfoList.add(newsContentInfo);
        }
        if (!CollectionUtils.isEmpty(newsContentInfoList)) {
            newsContentInfoMapper.insertList(newsContentInfoList);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public News queryNews(String newsSign) {
        NewsInfo newsInfo = newsInfoMapper.selectWithSign(newsSign);
        if (newsInfo == null) {
            return null;
        }
        NewsContentInfo contentInfo = newsContentInfoMapper.selectBySign(newsSign);
        News news = new News();
        BeanUtils.copyProperties(newsInfo, news);
        news.setContent(contentInfo.getContent());

        return news;
    }

    @Override
    @Transactional(readOnly = true)
    public List<News> queryList(Modules moduleType, NewsType newsType, Integer limit) {
        //设置默认的查询limit
        if (limit == null || limit == 0) {
            limit = 5;
        }
        List<NewsInfo> newsInfoList = newsInfoMapper.selectListWithNewType(moduleType.getValue(), newsType.getVal(), limit);

        List<News> newsList = null;
        if (!CollectionUtils.isEmpty(newsInfoList)) {
            newsList = new ArrayList<>();
            for (NewsInfo newsInfo : newsInfoList) {
                News news = new News();
                BeanUtils.copyProperties(newsInfo, news);
                newsList.add(news);
            }
        }
        return newsList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<News> queryList(Modules moduleType, NewsType newsType, Integer page, Integer limit) {
        Integer start = 0;
        if (page != 0 && limit != 0) {
            start = (page - 1) * limit;
        }
        List<NewsInfo> newsInfoList = newsInfoMapper.selectListWithNewTypeByPage(moduleType.getValue(), newsType.getVal(), start, limit);

        List<News> newsList = null;
        if (!CollectionUtils.isEmpty(newsInfoList)) {
            newsList = new ArrayList<>();
            for (NewsInfo newsInfo : newsInfoList) {
                News news = new News();
                BeanUtils.copyProperties(newsInfo, news);
                newsList.add(news);
            }
        }
        return newsList;
    }
}
