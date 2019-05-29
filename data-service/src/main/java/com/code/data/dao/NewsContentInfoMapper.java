package com.code.data.dao;

import com.code.data.beans.NewsContentInfo;

public interface NewsContentInfoMapper {
    int deleteByPrimaryKey(Long id);

    NewsContentInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(NewsContentInfo record);

    int updateByPrimaryKeyWithBLOBs(NewsContentInfo record);

    int updateByPrimaryKey(NewsContentInfo record);
}