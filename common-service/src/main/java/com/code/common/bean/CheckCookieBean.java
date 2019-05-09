package com.code.common.bean;

import com.code.common.enums.MatcherType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckCookieBean {
    private String checkCookieUrl;

    private MatcherType matcheType;

    private String matcherStr;

    private String cookie;

    public CheckCookieBean(String checkCookieUrl, MatcherType matcheType, String matcherStr) {
        this.checkCookieUrl = checkCookieUrl;
        this.matcheType = matcheType;
        this.matcherStr = matcherStr;
    }
}
