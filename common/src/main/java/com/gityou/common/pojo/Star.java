package com.gityou.common.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "star")
public class Star {
    @Id
    private Integer user;
    @Id
    private Long repository;

    private Integer watch;
    private Integer watchTime;
    private Boolean star;
    private Integer starTime;

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
}
