package com.code.common.bean;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProcessInfo {
    private String pluginName;

    private Byte modulesType;

    private String spiderWebsite;

    private LocalDateTime spiderDate;

    private Byte stage;

    private String remark;
}
