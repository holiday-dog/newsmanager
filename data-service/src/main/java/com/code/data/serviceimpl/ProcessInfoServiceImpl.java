package com.code.data.serviceimpl;

import com.code.common.enums.Modules;
import com.code.common.utils.DateUtils;
import com.code.common.utils.JsonPathUtils;
import com.code.data.beans.ProcessInfo;
import com.code.data.dao.ProcessInfoMapper;
import com.code.data.service.ProcessInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;


@Service
public class ProcessInfoServiceImpl implements ProcessInfoService {
    @Resource
    private ProcessInfoMapper processInfoMapper;
    private Logger logger = LoggerFactory.getLogger(ProcessInfoServiceImpl.class);


    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, timeout = 10000)
    public int buildAneSave(String json) {
        if (StringUtils.isEmpty(json)) {
            logger.info("json is empty");
        }
        ProcessInfo info = new ProcessInfo();

        info.setStage(Byte.parseByte(JsonPathUtils.getValue(json, "$.stage")));
        info.setSpiderDate(DateUtils.parseDateTime(JsonPathUtils.getValue(json, "$.spiderDate")));
        info.setSpiderWebsite(JsonPathUtils.getValue(json, "$.spiderWebsite"));
        info.setPluginName(JsonPathUtils.getValue(json, "$.pluginName"));
        info.setModulesType(Modules.parse(JsonPathUtils.getValue(json, "$.moduleType")).getValue());

        return processInfoMapper.insert(info);
    }

    @Override
    @Transactional(readOnly = true)
    public ProcessInfo selectByPrimaryKey(Long id) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProcessInfo> selectListOrderSpiderTime(Integer start, Integer limit) {
        if (start == null) {
            start = 0;
        }
        if (limit == null || limit == 0) {
            limit = 10;
        }
        return processInfoMapper.selectListOrderSpiderTime(start, limit);
    }
}
