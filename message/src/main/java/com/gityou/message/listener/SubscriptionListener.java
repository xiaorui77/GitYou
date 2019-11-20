package com.gityou.message.listener;

import com.gityou.common.pojo.SubscriptionIssue;
import com.gityou.common.pojo.SubscriptionRepository;
import com.gityou.message.service.SubscriptionService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionListener {

    @Autowired
    private SubscriptionService subscriptionService;

    /*
     * watch repository消息
     *
     * */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(value = "subscription.service", ignoreDeclarationExceptions = "true"),
            value = @Queue(value = "subscription.watch.create", durable = "true"),
            key = {"watch.create"}))
    public void subscribeWatch(SubscriptionRepository subscription) {
        subscriptionService.createWatch(subscription);
    }

    /*
     * update和delete
     * */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(value = "subscription.service", ignoreDeclarationExceptions = "true"),
            value = @Queue(value = "subscription.watch.update", durable = "true"),
            key = {"watch.update", "watch.delete"}))
    public void subscribeWatchUpdate(SubscriptionRepository subscription) {
        subscriptionService.updateWatch(subscription);
    }


    /*
     * issue订阅
     * */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(value = "subscription.service", ignoreDeclarationExceptions = "true"),
            value = @Queue(value = "subscription.issue.create", durable = "true"),
            key = {"issue.create"}))
    public void subscribeIssue(SubscriptionIssue subscription) {
        subscriptionService.createIssue(subscription);
    }

    /*
     *  update和delete
     * */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(value = "subscription.service", ignoreDeclarationExceptions = "true"),
            value = @Queue(value = "subscription.issue.update", durable = "true"),
            key = {"issue.update", "issue.delete"}))
    public void subscribeIssueUpdate(SubscriptionIssue subscription) {
        subscriptionService.updateIssue(subscription);
    }

}// end
