package com.gityou.message.service;

import com.gityou.common.pojo.Subscription;
import com.gityou.message.mapper.SubscriptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {
    @Autowired
    private SubscriptionMapper subscriptionMapper;


    public void createWatch(Subscription subscription) {

        // 获取用户的默认channel
        int channel = 1;    // 1: 站内 2: 邮箱

        subscription.setChannel(channel);
        subscriptionMapper.insert(subscription);
    }

}// end
