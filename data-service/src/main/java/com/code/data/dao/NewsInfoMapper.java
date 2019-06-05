package com.code.data.dao;

import com.code.data.beans.NewsInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NewsInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(NewsInfo record);

    int insertList(List<NewsInfo> infos);

    int insertSelective(NewsInfo record);

    NewsInfo selectByPrimaryKey(Long id);

    NewsInfo selectWithSign(String sign);

    List<NewsInfo> selectListWithNewType(@Param("modulesType") Byte moduleType, @Param("newsType") Byte newsType, @Param("limit") Integer limit);

    List<NewsInfo> selectListWithNewTypeByPage(@Param("modulesType") Byte moduleType, @Param("newsType") Byte newsType, @Param("start") Integer start, @Param("limit") Integer limit);

    int updateByPrimaryKeySelective(NewsInfo record);
}