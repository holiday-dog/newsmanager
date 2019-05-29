package com.code.data.dao;

import com.code.data.beans.NewsHotInfo;

public interface NewsHotInfoMapper {
    int deleteByPrimaryKey(Long id);

    NewsHotInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(NewsHotInfo record);

    int updateByPrimaryKey(NewsHotInfo record);
}