package com.gityou.repository.pojo;

public class IssueExtra extends Issue {
    private String authorAvatar;    // 头像url

    public IssueExtra(Issue issue) {
        super();
    }


    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }
}
