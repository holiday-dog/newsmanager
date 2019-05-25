package com.code.common.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

//登录代理的参数
@Data
@AllArgsConstructor
public class LoginParam {
    private String username;

    private String password;

    private String bindPhone;

    private String bindEmail;

    private String proxyUserId;

    private String proxyUrl;

    private String others;

    public LoginParam(String username, String password, String bindPhone) {
        this.username = username;
        this.password = password;
        this.bindPhone = bindPhone;
    }

    public LoginParam(String username, String password, String bindPhone, String bindEmail) {
        this.username = username;
        this.password = password;
        this.bindPhone = bindPhone;
        this.bindEmail = bindEmail;
    }
}
