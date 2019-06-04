package com.code.data.beans;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ProcessInfo {
    private Long id;

    private String pluginName;

    private Byte modulesType;

    private String spiderWebsite;

    private LocalDateTime spiderDate;

    private Byte stage;

    private String remark;

    private Date createDate;

    private Date lastUpdateDate;


}