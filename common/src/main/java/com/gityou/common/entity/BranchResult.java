package com.gityou.common.entity;

import java.util.Date;

public class BranchResult {
    private String name;
    private String author;
    private Integer time;   // 最后一次提交

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public void setTime(Date time) {
        this.time = Math.toIntExact(time.getTime() / 1000);
    }
}
