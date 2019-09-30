package com.gityou.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Table;
import java.sql.Timestamp;


@Table(name = "user")
public class User {
    private int id;
    private String username;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String salt;
    private String email;
    private Timestamp createTime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
