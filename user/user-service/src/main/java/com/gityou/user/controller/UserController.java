package com.gityou.user.controller;

import com.gityou.user.pojo.User;
import com.gityou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * User Controller
 * */
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    UserService userService;

    /*
     * 验证 User
     * 用户不存在: 返回null
     * 只有username password为空: 密码错误
     * */
    @PostMapping("auth")
    public ResponseEntity<User> authUser(String username, String password) {
        User user = userService.authUser(username, password);
        if (user == null)
            return ResponseEntity.badRequest().build();
        else if (user.getPassword() == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        else
            return ResponseEntity.ok(user);
    }

}// end
