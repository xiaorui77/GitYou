package com.gityou.message.service;

import com.gityou.common.pojo.Settings;
import com.gityou.common.pojo.SubscriptionRepository;
import com.gityou.message.mapper.SettingsMapper;
import com.gityou.message.mapper.SubscriptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
public class SubscriptionService {
    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @Autowired
    private SettingsMapper settingsMapper;


    // watch 订阅
    public void createWatch(SubscriptionRepository subscription) {
        // 如果是0代表默认没有, 为了节省查询时间不添加
        if (subscription.getType() == 0)
            return;

        // 获取用户的默认channel
        Example example = new Example(Settings.class);
        Example.Criteria criteria = example.createCriteria();
        example.selectProperties("notificationWatching");
        criteria.andEqualTo("user", subscription.getUser());
        int channel = settingsMapper.selectOneByExample(example).getNotificationWatching();

        // 添加到 watch订阅表
        subscription.setChannel(channel);
        subscriptionMapper.insert(subscription);
    }

    // watch 更新
    public void updateWatch(SubscriptionRepository subscription) {
        // 如果type为0, 则为删除
        if (subscription.getType() == 0) {
            subscription.setType(null);
            subscriptionMapper.delete(subscription);
        } else {
            subscriptionMapper.updateByPrimaryKeySelective(subscription);
        }
    }


}// end
