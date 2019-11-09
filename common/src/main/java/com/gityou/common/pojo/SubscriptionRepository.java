package com.gityou.common.pojo;


import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "subscription_repository")
public class SubscriptionRepository implements Serializable {
    @Id
    private Integer user;
    @Id
    private Long repository;
    private Integer type;
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

    public Long getRepository() {
        return repository;
    }

    public void setRepository(Long repository) {
        this.repository = repository;
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
                ", source=" + repository +
                ", channel=" + channel +
                '}';
    }
}
