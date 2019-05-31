package com.code.web.handler;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

//@ControllerAdvice
public class ExceptionHandle {
//    @ExceptionHandler(Exception.class)
    public ModelAndView handlerException(Exception e) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        String errorMsg = "系统异常，请稍后重试";
        if (StringUtils.isNotEmpty(e.getMessage())) {
            errorMsg = e.getMessage();
        }
        modelAndView.addObject("errorMsg", errorMsg);
        return modelAndView;
    }
}
