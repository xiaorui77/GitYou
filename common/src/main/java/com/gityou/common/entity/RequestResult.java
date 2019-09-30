package com.gityou.common.entity;

public class RequestResult {
    private Integer code;
    private String msg;

    public RequestResult() {
    }

    public RequestResult(Integer code) {
        this.code = code;
    }

    public RequestResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static RequestResult ok() {
        return new RequestResult(200, "请求成功");
    }

    public static RequestResult build() {
        return new RequestResult();
    }

    public static RequestResult build(int code) {
        return new RequestResult(code);
    }

    public static RequestResult build(int code, String msg) {
        return new RequestResult(code, msg);
    }

    public Integer getCode() {
        return code;
    }

    public RequestResult setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public RequestResult RequestResult(String msg) {
        this.msg = msg;
        return this;
    }
}
