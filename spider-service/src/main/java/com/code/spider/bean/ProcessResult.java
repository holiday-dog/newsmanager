package com.code.spider.bean;

import lombok.Data;

@Data
public class ProcessResult {
    private ProcessType processType;

    private Status processStatus;

    public enum ProcessType {
        LOGIN("执行登录"), SPIDER("执行抓取"), SEND("执行发送"), FINISH("执行结束");

        private String msg;


        ProcessType(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        public static ProcessType parse(String msg) {

            for (ProcessType type : values()) {
                if (type.getMsg().equals(msg)) {
                    return type;
                }
            }
            return null;
        }
    }

    public enum Status {
        PROCESS, SUCCESS, FAIL;
    }
}
