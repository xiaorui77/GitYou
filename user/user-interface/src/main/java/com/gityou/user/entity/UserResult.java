package com.gityou.user.entity;

public class UserResult<T> {
    private Integer code;
    private String msg;
    private T data;

    public UserResult() {
    }

    public UserResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public UserResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static UserResult build(int code, String msg) {
        return new UserResult(code, msg);
    }

    public Integer getCode() {
        return code;
    }

    public UserResult setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public UserResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public UserResult setData(T data) {
        this.data = data;
        return this;
    }
}
