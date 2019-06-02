package com.code.data.dao;

import com.code.data.beans.NewsHotInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NewsHotInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(NewsHotInfo record);

    int insertList(List<NewsHotInfo> hotInfoList);

    NewsHotInfo selectByPrimaryKey(Long id);

    List<NewsHotInfo> selectListBylimit(@Param("modules") Byte modules, @Param("limit") int limit);

}