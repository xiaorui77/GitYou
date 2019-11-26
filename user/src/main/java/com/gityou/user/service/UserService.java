package com.gityou.user.service;


import com.gityou.common.mapper.EmailMapper;
import com.gityou.common.pojo.Email;
import com.gityou.common.pojo.User;
import com.gityou.user.mapper.UserMapper;
import com.gityou.user.util.CodecUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmailMapper emailMapper;


    // 登录验证 User 每次都要修改salt? 暂时保留多地方登录
    public User loginUser(String username, String password) {
        User record = new User();
        record.setUsername(username);

        record = userMapper.selectOne(record);
        if (record == null)
            return null;

        // 获取salt
        String shaHex = CodecUtils.shaHex(password, record.getSalt());
        if (shaHex.equals(record.getPassword()))
            return record;
        else {
            record.setPassword(null);
            return record;
        }
    }

    // 根据 id获取user信息
    public User queryUser(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    // 根据 username获取user信息
    public User queryByUsername(String username) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);

        return userMapper.selectOneByExample(example);
    }

    // 根据email获取user信息
    public User queryByEmail(String email) {
        User record = new User();
        record.setEmail(email);

        record = userMapper.selectOne(record);
        if (record != null)
            return record;

        // 从email表中查询
        Email userEmail = new Email();
        userEmail.setEmail(email);

        userEmail = emailMapper.selectOne(userEmail);
        if (userEmail == null)
            return null;

        return userMapper.selectByPrimaryKey(userEmail.getUser());
    }

    // 根据emails 查询用户名
    @Transactional
    public Map<String, String> queryUsersByEmail(Set<String> emails) {
        Map<String, String> result = new HashMap<>();
        if (emails == null || emails.isEmpty())
            return result;

        // 先从uesr表中查询
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("email", emails);
        example.selectProperties("username", "email");

        List<User> users = userMapper.selectByExample(example);
        users.forEach(e -> {
            result.put(e.getEmail(), e.getUsername());
            emails.remove(e.getEmail());
        });

        // user表中未找到的去email表查询
        List<User> remainingUsers = userMapper.queryUsersByEmails(emails);
        remainingUsers.forEach(e -> {
            result.put(e.getEmail(), e.getUsername());
            emails.remove(e.getEmail());
        });

        return result;
    }

    // 根据id 查询username
    public Map<Integer, String> queryNames(Set<Integer> ids) {
        Map<Integer, String> result = new HashMap<>();

        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", ids);
        example.selectProperties("id", "username");

        List<User> users = userMapper.selectByExample(example);
        users.forEach(e -> {
            result.put(e.getId(), e.getUsername());
        });
        return result;
    }

    // 根据id获取 Users
    public Map<Integer, User> queryUsers(Set<Integer> ids) {
        Map<Integer, User> result = new HashMap<>();
        if (ids.isEmpty())
            return result;

        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", ids);

        List<User> users = userMapper.selectByExample(example);
        users.forEach(e -> result.put(e.getId(), e));
        return result;
    }

}// end
