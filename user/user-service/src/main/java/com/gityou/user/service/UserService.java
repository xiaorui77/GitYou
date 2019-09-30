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


    // 验证User
    public User authUser(String username, String password) {
        User user = new User();
        user.setUsername(username);

        User record = userMapper.selectOne(user);
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
