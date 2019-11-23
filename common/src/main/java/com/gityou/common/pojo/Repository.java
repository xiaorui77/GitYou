package com.gityou.common.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;


@Table(name = "repository")
public class Repository {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // 保证insert时能够返回id
    private Long id;
    private Integer userId;
    private String username;
    private String name;
    private String description;

    @JsonIgnore
    private Long machine;
    private Byte privacy;
    private Integer type;
    private Integer language;

    private Integer issueNum;
    private Integer watch;
    private Integer star;
    private Integer fork;
    private String defaultBranch;
    private Timestamp updateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Long getMachine() {
        return machine;
    }

    public void setMachine(Long machine) {
        this.machine = machine;
    }

    public Byte getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Byte privacy) {
        this.privacy = privacy;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public Integer getIssueNum() {
        return issueNum;
    }

    public void setIssueNum(Integer issueNum) {
        this.issueNum = issueNum;
    }

    public Integer getWatch() {
        return watch;
    }

    public void setWatch(Integer watch) {
        this.watch = watch;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public Integer getFork() {
        return fork;
    }

    public void setFork(Integer fork) {
        this.fork = fork;
    }

    public String getDefaultBranch() {
        return defaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
