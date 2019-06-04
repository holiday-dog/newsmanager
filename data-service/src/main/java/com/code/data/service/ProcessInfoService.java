package com.code.data.service;

import com.code.data.beans.ProcessInfo;

import java.util.List;

public interface ProcessInfoService {
    int buildAneSave(String json);

    ProcessInfo selectByPrimaryKey(Long id);

    List<ProcessInfo> selectListOrderSpiderTimeByPage(Integer page, Integer limit);

}
