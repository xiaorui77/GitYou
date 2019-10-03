package com.gityou.repository.entity;

/* 返回 */
public class FileResult {
    private String name;
    private Boolean folder;
    private String message; // 最后一次提交信息
    private Integer time;  // 最后一次提交时间

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getFolder() {
        return folder;
    }

    public void setFolder(Boolean folder) {
        this.folder = folder;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }
}
