package com.code.data.utils;

import com.code.common.bean.ResponseData;

public class ResponseUtils {
    public static void setStatus(ResponseData responseData, ResultStatus resultStatus) {
        responseData.setResultCode(resultStatus.getCode());
        responseData.setResultMsg(resultStatus.getMsg());
    }
}
