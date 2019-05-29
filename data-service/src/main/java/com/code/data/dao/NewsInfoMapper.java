package com.code.data.dao;

import com.code.data.beans.NewsInfo;

public interface NewsInfoMapper {
    int deleteByPrimaryKey(Integer id);

    NewsInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(NewsInfo record);

    int updateByPrimaryKey(NewsInfo record);
}