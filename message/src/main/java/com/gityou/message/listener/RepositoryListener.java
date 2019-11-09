package com.gityou.message.listener;

import com.gityou.common.pojo.SubscriptionRepository;
import com.gityou.message.service.SubscriptionService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RepositoryListener {

    @Autowired
    private SubscriptionService subscriptionService;

    /*
     * 从MQ中获取消息订阅通知(也可直接调用本服务的相应接口, 待定)
     *
     * 用户在关注Repository, issue, PR等时产生订阅消息, 由此消费, 即响应用户的订阅
     * */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(value = "subscription.service", ignoreDeclarationExceptions = "true"),
            value = @Queue(value = "subscription.watch.create", durable = "true"),
            key = {"watch.create"}
    ))
    public void subscribeWatch(SubscriptionRepository subscription) {
        subscriptionService.createWatch(subscription);
    }

    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(value = "subscription.service", ignoreDeclarationExceptions = "true"),
            value = @Queue(value = "subscription.watch.update", durable = "true"),
            key = {"watch.update", "watch.delete"}
    ))
    public void subscribeWatchUpdate(SubscriptionRepository subscription) {
        subscriptionService.updateWatch(subscription);
    }


    /*
     * 从消息队列中获取issue通知
     *
     * 当有新issue产生时会发布消息, 然后由此捕获, 计算订阅了该项目的用户并推送
     * */
    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(value = "subscription.update", ignoreDeclarationExceptions = "true"),
            value = @Queue(value = "issue.update", durable = "true"),
            key = {"issue"}
    ))
    public void issueNews(@Headers Map<String, String> msg) {
        if (msg == null || msg.size() == 0)
            return;
        System.out.println("收到了消息: " + msg);
    }

}// end
