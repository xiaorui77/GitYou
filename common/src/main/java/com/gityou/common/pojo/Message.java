package com.gityou.common.pojo;


import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "message")
public class Message {
    @Id
    private Long id;
    private Integer user;
    private Long repository;
    private String content;

    private Integer type;
    private Integer status;
    private Integer createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public Long getRepository() {
        return repository;
    }

    public void setRepository(Long repository) {
        this.repository = repository;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }
}
