package com.gityou.common.entity;

public class ResponseResult<T> {
    private Integer code;
    private String msg;
    private T data;

    public ResponseResult() {
    }

    public ResponseResult(Integer code) {
        this.code = code;
    }

    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ResponseResult ok() {
        return new ResponseResult(200, "请求成功");
    }

    public static ResponseResult ok(String msg) {
        return new ResponseResult(200, msg);
    }

    public static ResponseResult ok(Object data) {
        return new ResponseResult(200, "请求成功").setData(data);
    }

    public static ResponseResult fail(String msg) {
        return new ResponseResult(400, msg);
    }

    public static ResponseResult build() {
        return new ResponseResult();
    }

    public static ResponseResult build(int code) {
        return new ResponseResult(code);
    }

    public static ResponseResult build(int code, String msg) {
        return new ResponseResult(code, msg);
    }


    public Integer getCode() {
        return code;
    }

    public ResponseResult setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResponseResult RequestResult(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResponseResult setData(T data) {
        this.data = data;
        return this;
    }
}
