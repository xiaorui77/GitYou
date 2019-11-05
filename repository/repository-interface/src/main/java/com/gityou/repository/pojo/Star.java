package com.gityou.repository.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "star")
public class Star {
    @Id
    private Integer id;
    @Id
    private Long repository;
    private Boolean star;
    private Integer starTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getRepository() {
        return repository;
    }

    public void setRepository(Long repository) {
        this.repository = repository;
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
