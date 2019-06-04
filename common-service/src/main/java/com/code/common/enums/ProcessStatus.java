package com.code.common.enums;

import org.apache.commons.lang3.StringUtils;

public enum ProcessStatus {
    SPIDER_FAIL((byte) 0, "爬取失败"), SPIDER_SUCCESS((byte) 1, "爬取成功"),

    HANDLE_FAIL((byte) 2, "解析失败"), HANDLE_SUCCESS((byte) 3, "解析成功"),

    FINISH_SUCCESS((byte) 4, "成功"), UNKOWN((byte) -1, "未知");

    private String msg;
    private Byte val;

    ProcessStatus(Byte val, String msg) {
        this.msg = msg;
        this.val = val;
    }

    public String getMsg() {
        return msg;
    }

    public Byte getVal() {
        return val;
    }


    public static ProcessStatus parse(Byte value) {
        if (value == null) {
            return UNKOWN;
        }
        for (ProcessStatus status : values()) {
            if (status.getVal().equals(value)) {
                return status;
            }
        }
        return UNKOWN;
    }

    public static ProcessStatus parse(String msg) {
        if (StringUtils.isEmpty(msg)) {
            return UNKOWN;
        }
        for (ProcessStatus status : values()) {
            if (status.getMsg().equals(msg)) {
                return status;
            }
        }
        return UNKOWN;
    }

    public static ProcessStatus parseStr(String value) {
        if (value == null) {
            return UNKOWN;
        }
        Byte val = Byte.parseByte(value);
        for (ProcessStatus status : values()) {
            if (status.getVal().equals(val)) {
                return status;
            }
        }
        return UNKOWN;
    }
    //    STORE_FAIL, STORE_SUCCESS,
//
//    ANALY_FAIL, ANALY_SUCCESS;
}
