package com.code.data.dao;

import com.code.data.beans.NewsHotInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NewsHotInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(NewsHotInfo record);

    int insertSelective(NewsHotInfo record);

    NewsHotInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(NewsHotInfo record);

    int updateByPrimaryKey(NewsHotInfo record);
}