package com.gityou.common.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "branch")
public class Branch {
    @Id
    private Long id;
    private Long repository;
    private String name;
    private Integer createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRepository() {
        return repository;
    }

    public void setRepository(Long repository) {
        this.repository = repository;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }
}
