package com.gityou.common.pojo;


import java.io.Serializable;

public class Subscription implements Serializable {

    private Integer user;

    private Integer type;

    private Long source;
    private Integer channel;


    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getSource() {
        return source;
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "user=" + user +
                ", type=" + type +
                ", source=" + source +
                ", channel=" + channel +
                '}';
    }
}
