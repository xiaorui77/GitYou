package com.gityou.message.service;

import com.gityou.common.mapper.EmailMapper;
import com.gityou.common.mapper.MessageMapper;
import com.gityou.common.pojo.*;
import com.gityou.message.mapper.SubscriptionIssueMapper;
import com.gityou.message.mapper.SubscriptionRepositoryMapper;
import com.gityou.message.utils.EmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RepositoryService {
    @Autowired
    private SubscriptionRepositoryMapper subscriptionRepositoryMapper;

    @Autowired
    private SubscriptionIssueMapper subscriptionIssueMapper;

    @Autowired
    private EmailMapper emailMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private EmailUtils emailUtils;


    public void issueCreate(Issue issue) {
        // todo: 发给watch了该仓库的用户
        SubscriptionRepository subscription = new SubscriptionRepository();
        subscription.setRepository(issue.getRepository());
        List<SubscriptionRepository> subscribers = subscriptionRepositoryMapper.select(subscription);

        for (SubscriptionRepository subscriber : subscribers) {
            String success = null;
            String message = "您好, 您关注的仓库(" + subscriber.getRepository() + ")有新的issue了!";

            if ((subscriber.getChannel() | 0x1) == 1) {
                Message messageWeb = new Message();
                messageWeb.setUser(subscriber.getUser());
                messageWeb.setRepository(issue.getRepository());
                messageWeb.setTarget(issue.getRepository());
                messageWeb.setContent(message);
                messageWeb.setCreateTime((int) (System.currentTimeMillis() / 1000));

                if (messageMapper.insertSelective(messageWeb) != 1)
                    success = "消息创建失败";

            } else if ((subscriber.getChannel() | 0x2) == 2) {
                String email = emailMapper.queryEmail(subscriber.getUser());
                // 发送邮件
                if (emailUtils.simpleMailSend(email, "[GitYou]您关注的仓库有新的issue了!", message))
                    success = "邮件发送失败";
            }

            if (success != null)
                System.out.println("失败: " + success + " --- " + subscriber);
        }
    }

    public void issueCommentCreate(IssueComment comment) {
        // todo: 发给关注了该issue的人

        // 所属的repository
        Long repositoryId = subscriptionIssueMapper.queryRepository(comment.getIssue());

        // 订阅了该issue的用户
        SubscriptionIssue subscription = new SubscriptionIssue();
        subscription.setIssue(comment.getIssue());
        List<SubscriptionIssue> subscribers = subscriptionIssueMapper.select(subscription);

        for (SubscriptionIssue subscriber : subscribers) {
            String success = null;
            String message = "您好, 您关注的Issue(" + subscriber.getIssue() + ")有新的评论了!";

            if ((subscriber.getChannel() | 0x1) == 1) {
                Message messageWeb = new Message();
                messageWeb.setUser(subscriber.getUser());
                messageWeb.setRepository(repositoryId);
                messageWeb.setTarget(comment.getIssue());
                messageWeb.setContent(message);
                messageWeb.setCreateTime((int) (System.currentTimeMillis() / 1000));

                if (messageMapper.insertSelective(messageWeb) != 1)
                    success = "消息创建失败";

            } else if ((subscriber.getChannel() | 0x2) == 2) {
                String email = emailMapper.queryEmail(subscriber.getUser());
                // 发送邮件
                if (emailUtils.simpleMailSend(email, "[GitYou]您关注的Issue有新的评论了!", message))
                    success = "邮件发送失败";
            }

            if (success != null)
                System.out.println("失败: " + success + " --- " + subscriber);
        }
    }

}// end
