package com.code.common.enums;

public enum ProcessStatus {
    SPIDER_FAIL((byte) 0, "爬取失败"), SPIDER_SUCCESS((byte) 1, "爬取成功"),

    HANDLE_FAIL((byte) 2, "解析失败"), HANDLE_SUCCESS((byte) 3, "解析成功"),

    FINISH_SUCCESS((byte) 4, "成功");

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

    //    STORE_FAIL, STORE_SUCCESS,
//
//    ANALY_FAIL, ANALY_SUCCESS;
}
