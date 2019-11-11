package com.gityou.common.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "email")
public class Email {
    @Id
    private Long id;
    private Integer user;
    private String email;
    private Integer type;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
