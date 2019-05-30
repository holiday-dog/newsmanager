package com.code.common.enums;

import org.apache.commons.lang3.StringUtils;

public enum NewsType {
    LATEST((byte) 1, "最新的"), TOP((byte) 2, "排行"), HISTORY((byte) 3, "历史");

    private Byte val;

    private String msg;

    NewsType(Byte val, String msg) {
        this.val = val;
        this.msg = msg;
    }

    public Byte getVal() {
        return val;
    }

    public String getMsg() {
        return msg;
    }

    public static NewsType parse(String msg) {
        if (StringUtils.isEmpty(msg)) {
            return LATEST;
        }
        for (NewsType newsType : values()) {
            if (newsType.getMsg().equals(msg)) {
                return newsType;
            }
        }
        return LATEST;
    }
}
