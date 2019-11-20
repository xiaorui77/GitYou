package com.gityou.common.pojo;


import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "subscription_issue")
public class SubscriptionIssue {
    @Id
    private Integer user;
    @Id
    private Long issue;
    private Integer channel;

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public Long getIssue() {
        return issue;
    }

    public void setIssue(Long issue) {
        this.issue = issue;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }
}
