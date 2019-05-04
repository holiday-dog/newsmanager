package com.code.common.crawl;

public enum RequestMethod {
    GET("get"), POST("post"), POST_STRING("post_string"), POST_BYTE("post_byte"), POST_FILE("post_file");
    private String val;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    RequestMethod(String val) {
        this.val = val;
    }
}
