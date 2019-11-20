package com.gityou.user.service;


import com.gityou.common.entity.ResponseResult;
import com.gityou.common.entity.UserInfo;
import com.gityou.common.utils.JwtUtils;
import com.gityou.user.config.AuthProperties;
import com.gityou.common.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthProperties authProperties;


    public ResponseResult<User> authentication(String username, String password) {

        User user = this.userService.loginUser(username, password);

        if (user == null)
            return new ResponseResult<>(400, "用户不存在");
        if (user.getPassword() == null)
            return new ResponseResult<>(401, "密码不正确");

        try {
            // 生成token
            String token = JwtUtils.generateToken(new UserInfo(user.getId(), user.getUsername()), authProperties.getPrivateKey(), authProperties.getExpire() * 60);
            return new ResponseResult<>(200, token, user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseResult<>(500, "创建token失败");
    }

}// end
