package com.code.common.utils;

import com.code.common.bean.ResponseData;
import com.code.common.enums.ResultStatus;

public class ResponseUtils {
    public static void setStatus(ResponseData responseData, ResultStatus resultStatus) {
        responseData.setResultCode(resultStatus.getCode());
        responseData.setResultMsg(resultStatus.getMsg());
    }
}
