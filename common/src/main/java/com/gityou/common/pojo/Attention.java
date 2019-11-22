package com.gityou.common.pojo;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "attention")
public class Attention {
    @Id
    private Integer user;
    @Id
    private Long repository;

    private Integer watch;
    private Integer watchTime;
    private Boolean star;
    private Integer starTime;
    private Boolean fork;
    private Integer forkTime;

    @Transient
    private String username;
    @Transient
    private String avatar;


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


    public Integer getWatch() {
        return watch;
    }

    public void setWatch(Integer watch) {
        this.watch = watch;
    }

    public Integer getWatchTime() {
        return watchTime;
    }

    public void setWatchTime(Integer watchTime) {
        this.watchTime = watchTime;
    }

    public Boolean getStar() {
        return star;
    }

    public void setStar(Boolean star) {
        this.star = star;
    }

    public Integer getStarTime() {
        return starTime;
    }

    public void setStarTime(Integer starTime) {
        this.starTime = starTime;
    }

    public Boolean getFork() {
        return fork;
    }

    public void setFork(Boolean fork) {
        this.fork = fork;
    }

    public Integer getForkTime() {
        return forkTime;
    }

    public void setForkTime(Integer forkTime) {
        this.forkTime = forkTime;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
