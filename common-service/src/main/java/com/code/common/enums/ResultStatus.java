package com.code.common.enums;

public enum ResultStatus {
    SUCCESS("1000", "请求成功"),
    NOT_ENOUGH_PARAMS("1001", "参数不完整"),
    SYSTEM_ERROR("1005", "系统异常"),
    ;

    private String code;
    private String msg;

    ResultStatus(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
