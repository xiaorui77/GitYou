package com.gityou.user.service;


import com.gityou.common.entity.UserInfo;
import com.gityou.common.mapper.EmailMapper;
import com.gityou.common.mapper.SettingsMapper;
import com.gityou.common.pojo.Email;
import com.gityou.common.pojo.Settings;
import com.gityou.user.mapper.UserMapper;
import com.gityou.common.pojo.User;
import com.gityou.user.util.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SettingsService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SettingsMapper settingsMapper;

    @Autowired
    private EmailMapper emailMapper;


    // 查询用户设置
    public Settings query(Integer user) {
        // 用户验证
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        if (loginUser == null || !loginUser.getId().equals(user))
            return null;

        return settingsMapper.selectByPrimaryKey(user);
    }

    // 查询邮箱设置
    public List<Email> queryEmails(Integer userId) {
        // 用户验证
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        if (loginUser == null || !loginUser.getId().equals(userId))
            return null;

        Email email = new Email();
        email.setUser(userId);
        return emailMapper.select(email);
    }


    // 修改设置
    public Boolean modifyNotification(Integer user, String key, Integer value) {
        // 用户验证
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        if (loginUser == null || !loginUser.getId().equals(user))
            return null;

        // 判断在哪个表中
        String table = "settings";

        return settingsMapper.modify(user, table, key, value) == 1;
    }

    // 邮箱设置
    @Transactional
    public Boolean modifyEmail(Integer user, String key, String value) {
        // 用户验证
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        if (loginUser == null || !loginUser.getId().equals(user))
            return null;

        switch (key) {
            case "add":
                return addEmail(user, value);
            case "delete":
                Long emailId = Long.parseUnsignedLong(value);
                return deleteEmail(user, emailId);
            case "primary_change":
                return changePrimary(user, Long.parseUnsignedLong(value));
        }
        return null;
    }

    // 添加邮箱
    private Boolean addEmail(Integer user, String address) {
        Email email = new Email();
        email.setUser(user);
        email.setEmail(address);

        if (emailMapper.selectOne(email) != null)
            return false;

        return emailMapper.insertSelective(email) > 0;
    }

    // 删除邮箱
    private Boolean deleteEmail(Integer userId, Long id) {
        // 用户验证
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        if (loginUser == null || !loginUser.getId().equals(userId))
            return null;

        Email record = emailMapper.selectByPrimaryKey(id);
        if (record == null)
            return null;

        // 是否主邮箱
        if ((record.getType() & 0x20) == 0)
            return false;

        // 防止删除别人的
        Email email = new Email();
        email.setUser(userId);
        email.setId(id);
        return emailMapper.delete(email) == 1;
    }

    // 修改主邮箱
    private Boolean changePrimary(Integer userId, Long id) {
        // 用户验证
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        if (loginUser == null || !loginUser.getId().equals(userId))
            return null;

        // 获取原来的
        Email record = emailMapper.queryByType(userId, 32);

        // 如果已经设置
        if (record == null || record.getId().equals(id))
            return true;

        if (emailMapper.removeType(record.getId(), userId, 32) == 0)
            return false;

        if (emailMapper.addType(id, userId, 32) == 0)
            return false;

        // 获取新的email
        String email = emailMapper.selectByPrimaryKey(id).getEmail();

        User user = new User();
        user.setId(userId);
        user.setEmail(email);
        return userMapper.updateByPrimaryKeySelective(user) == 1;
    }


}// end
