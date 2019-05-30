package com.code.data.dao;

import com.code.data.beans.NewsContentInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NewsContentInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(NewsContentInfo record);

    int insertList(List<NewsContentInfo> contentInfoList);

    NewsContentInfo selectBySign(String newsSign);

}