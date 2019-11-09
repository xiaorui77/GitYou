package com.gityou.common.pojo;


import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "subscriptionIssue")
public class SubscriptionIssue {
    @Id
    private Integer user;
    @Id
    private Long repository;
    private Integer channel;

    public void setUser(Integer user) {
        this.user = user;
    }

    public void setRepository(Long repository) {
        this.repository = repository;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }
}
