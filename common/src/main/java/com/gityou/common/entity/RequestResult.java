package com.gityou.common.entity;

public class RequestResult<T> {
    private Integer code;
    private String msg;
    private T data;

    public RequestResult() {
    }

    public RequestResult(Integer code) {
        this.code = code;
    }

    public RequestResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public RequestResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
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

    public T getData() {
        return data;
    }

    public RequestResult setData(T data) {
        this.data = data;
        return this;
    }
}
