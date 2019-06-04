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

        byte val = (byte) JsonPathUtils.getObj(json, "$.stage", Integer.class).intValue();
        info.setStage(val);
        info.setSpiderDate(DateUtils.parseDateTime(JsonPathUtils.getValue(json, "$.spiderDate")));
        info.setSpiderWebsite(JsonPathUtils.getValue(json, "$.spiderWebsite"));
        info.setPluginName(JsonPathUtils.getValue(json, "$.pluginName"));
        info.setModulesType(Modules.parse(JsonPathUtils.getValue(json, "$.moduleType")).getValue());
        info.setRemark(JsonPathUtils.getValue(json, "$.remark"));

        return processInfoMapper.insert(info);
    }

    @Override
    @Transactional(readOnly = true)
    public ProcessInfo selectByPrimaryKey(Long id) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProcessInfo> selectListOrderSpiderTimeByPage(Integer page, Integer limit) {
        Integer start = 0;
        if (page != 0 && page != null && limit != 0 && limit != null) {
            start = (page - 1) * limit;
        }
        return processInfoMapper.selectListOrderSpiderTime(start, limit);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getTotalPage() {
        return processInfoMapper.getTotalPage();
    }
}
