package com.gityou.repository.pojo;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;


@Table(name = "repository")
public class Repository {
    @Id
    private Long id;
    private Integer userId;
    private String name;
    private String description;
    private Byte secret;
    private Timestamp createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Byte getSecret() {
        return secret;
    }

    public void setSecret(Byte secret) {
        this.secret = secret;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
