package com.colin.rabbitmq.topic.providerack.entities;

/**
 * Created by sxf on 2019-5-15.
 * 结果码枚举类
 */
public enum ResultCode {

    SUCCESS("0", "操作成功!"),
    FAILED("-1","操作失败"),
    UNKNOWN_ERROR("999","未知异常");

    /**
     * 结果码
     */
    private String code;

    /**
     * 结果码描述
     */
    private String msg;


    ResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}