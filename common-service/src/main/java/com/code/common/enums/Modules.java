package com.code.common.enums;

import java.io.Serializable;

public enum Modules implements Serializable {
    OTHERS((byte) 0, "其他"), SCIENCE((byte) 1, "科技"), EDUCATION((byte) 2, "教育"), TOUR((byte) 3, "旅游"), INTERNATIONAL((byte) 4, "国际");

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
}
