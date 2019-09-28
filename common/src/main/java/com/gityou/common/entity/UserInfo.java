package com.gityou.common.entity;

/**
 * 用户信息
 */
public class UserInfo {

    private Integer id;

    private String username;

    public UserInfo() {
    }

    public UserInfo(Integer id, String username) {
        this.id = id;
        this.username = username;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
