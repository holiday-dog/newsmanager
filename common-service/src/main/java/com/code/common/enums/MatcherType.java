package com.code.common.enums;

public enum MatcherType {
    CONTAINS("contains"), NOT_CONTAINS("not_contains"), MATCHER("matcher"), NOT_MATCHER("not_matcher");

    private String val;

    MatcherType(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
