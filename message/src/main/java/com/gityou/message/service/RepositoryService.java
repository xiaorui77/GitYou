package com.gityou.message.service;

import com.gityou.common.pojo.Issue;
import com.gityou.common.pojo.IssueComment;
import com.gityou.common.pojo.SubscriptionIssue;
import com.gityou.common.pojo.SubscriptionRepository;
import com.gityou.message.mapper.SubscriptionIssueMapper;
import com.gityou.message.mapper.SubscriptionRepositoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RepositoryService {
    @Autowired
    private SubscriptionRepositoryMapper subscriptionRepositoryMapper;

    @Autowired
    private SubscriptionIssueMapper subscriptionIssueMapper;


    public void issueCreate(Issue issue) {
        // todo: 发给watch了该仓库的用户
        SubscriptionRepository subscription = new SubscriptionRepository();
        subscription.setRepository(issue.getRepository());
        List<SubscriptionRepository> subscribers = subscriptionRepositoryMapper.select(subscription);

        for (SubscriptionRepository subscriber : subscribers) {
            System.out.println("发送消息=> " + subscriber.getUser() + "(issue): " + issue.getTitle());
        }
    }

    public void issueCommentCreate(IssueComment comment) {
        // todo: 发给关注了该issue的人
        SubscriptionIssue subscription = new SubscriptionIssue();
        subscription.setIssue(comment.getIssue());

        List<SubscriptionIssue> subscribers = subscriptionIssueMapper.select(subscription);

        for (SubscriptionIssue subscriber : subscribers) {
            System.out.println("发送消息=> " + subscriber.getUser() + "(issueComment): " + comment.getContent());
        }
    }

}// end
