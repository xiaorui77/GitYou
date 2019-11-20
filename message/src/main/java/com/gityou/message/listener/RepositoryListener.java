package com.gityou.message.listener;

import com.gityou.common.pojo.Issue;
import com.gityou.common.pojo.IssueComment;
import com.gityou.message.service.RepositoryService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class RepositoryListener {

    @Autowired
    private RepositoryService repositoryService;


    /*
     * issue创建时
     *
     * 通知订阅该repository的用户
     * */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(value = "repository.issue", ignoreDeclarationExceptions = "true"),
            value = @Queue(value = "repository.issue.create", durable = "true"),
            key = {"issue.create"}))
    public void subscribeWatch(Issue issue) {
        repositoryService.issueCreate(issue);
    }


    /*
     * issue下发表comment时
     *
     * 通知订阅该issue的用户
     * */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(value = "repository.issue", ignoreDeclarationExceptions = "true"),
            value = @Queue(value = "repository.issueComment.create", durable = "true"),
            key = {"issueComment.create"}))
    public void subscribeWatch(IssueComment comment) {
        repositoryService.issueCommentCreate(comment);
    }


}// end
