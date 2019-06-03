package com.code.data.dao;

import com.code.data.beans.ProcessInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProcessInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProcessInfo record);

    ProcessInfo selectByPrimaryKey(Long id);

    List<ProcessInfo> selectListOrderSpiderTime(@Param("start") Integer start, @Param("limit") Integer limit);

}