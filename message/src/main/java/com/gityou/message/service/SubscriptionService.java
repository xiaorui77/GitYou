package com.gityou.message.service;

import com.gityou.common.mapper.SettingsMapper;
import com.gityou.common.pojo.Settings;
import com.gityou.common.pojo.SubscriptionIssue;
import com.gityou.common.pojo.SubscriptionRepository;
import com.gityou.message.mapper.SubscriptionIssueMapper;
import com.gityou.message.mapper.SubscriptionRepositoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
public class SubscriptionService {
    @Autowired
    private SubscriptionRepositoryMapper subscriptionRepositoryMapper;

    @Autowired
    private SubscriptionIssueMapper subscriptionIssueMapper;

    @Autowired
    private SettingsMapper settingsMapper;


    // watch 订阅
    public void createWatch(SubscriptionRepository subscription) {
        // 如果是0代表默认没有, 为了节省查询时间不添加
        if (subscription.getType() == 0)
            return;

        // 获取用户的默认channel
        int channel = defaultChannel(subscription.getUser(), "notificationWatching");

        // 添加到 watch订阅表
        subscription.setChannel(channel);
        subscriptionRepositoryMapper.insert(subscription);
    }

    // watch 更新
    public void updateWatch(SubscriptionRepository subscription) {
        // 如果type为0, 则为删除
        if (subscription.getType() == 0) {
            subscription.setType(null);
            subscriptionRepositoryMapper.delete(subscription);
        } else {
            subscriptionRepositoryMapper.updateByPrimaryKeySelective(subscription);
        }
    }

    // issue 创建
    public void createIssue(SubscriptionIssue subscription) {
        // 默认channel
        int channel = defaultChannel(subscription.getUser(), "notificationParticipating");

        subscription.setChannel(channel);
        subscriptionIssueMapper.insert(subscription);
    }

    // issue 更新
    public void updateIssue(SubscriptionIssue subscription) {
        if (subscription.getChannel() == 0) {
            subscription.setChannel(null);
            subscriptionIssueMapper.delete(subscription);
        } else {
            subscriptionIssueMapper.updateByPrimaryKeySelective(subscription);  // todo: 复合主键有没有问题?
        }
    }


    // 获取用户默认的channel
    private int defaultChannel(Integer user, String type) {
        Example example = new Example(Settings.class);
        Example.Criteria criteria = example.createCriteria();
        example.selectProperties(type);
        criteria.andEqualTo("user", user);
        return settingsMapper.selectOneByExample(example).getNotificationWatching();
    }

}// end
