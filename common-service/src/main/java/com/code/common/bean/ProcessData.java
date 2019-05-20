package com.code.common.bean;

import com.code.common.enums.ProcessStatus;
import lombok.Data;

import java.util.Date;

@Data
public class ProcessData {
    private Date date;
    private ProcessStatus processStatus;
    private String pluginName;
    private String remak;
}
