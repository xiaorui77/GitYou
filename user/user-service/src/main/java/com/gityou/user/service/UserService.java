package com.gityou.user.service;


import com.gityou.user.mapper.UserMapper;
import com.gityou.user.pojo.User;
import com.gityou.user.util.CodecUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;


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

}// end
