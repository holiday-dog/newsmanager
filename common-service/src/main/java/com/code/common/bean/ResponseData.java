package com.code.common.bean;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData implements Serializable {
    private String resultCode;

    private String resultMsg;

    private Object resultData;


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
