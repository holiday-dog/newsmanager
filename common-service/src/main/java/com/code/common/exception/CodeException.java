package com.code.common.exception;

//自定义异常
public class CodeException extends RuntimeException {
    public CodeException(String message) {
        super(message);
    }

    public CodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
