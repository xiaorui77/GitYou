package com.gityou.repository.entity;

public class CommitResult {
    private String email;   // 提交者的邮箱, 用来标识提交者
    private String name;    // 提交号
    private String message; // 提交说明, 简单形式
    private String fullMessage; // 详细提交说明
    private Integer time;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFullMessage() {
        return fullMessage;
    }

    public void setFullMessage(String fullMessage) {
        this.fullMessage = fullMessage;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
}
