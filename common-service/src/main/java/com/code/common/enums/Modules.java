package com.code.common.enums;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public enum Modules implements Serializable {
    OTHERS((byte) 0, "其他"), SCIENCE((byte) 1, "科技"), EDUCATION((byte) 2, "教育"), TRAVEL((byte) 3, "旅游"), INTERNATIONAL((byte) 4, "国际");

    private Byte value;

    private String msg;

    Modules(Byte value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public Byte getValue() {
        return value;
    }

    public String getMsg() {
        return msg;
    }

    public static Modules parse(Byte value) {
        if (value == null) {
            return OTHERS;
        }
        for (Modules module : values()) {
            if (module.getValue().equals(value)) {
                return module;
            }
        }
        return OTHERS;
    }

    public static Modules parse(String msg) {
        if (StringUtils.isEmpty(msg)) {
            return OTHERS;
        }
        for (Modules module : values()) {
            if (module.getMsg().equals(msg)) {
                return module;
            }
        }
        return OTHERS;
    }
}
