package com.gityou.user.controller;

import com.gityou.user.pojo.User;
import com.gityou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
     * 根据id获取用户信息
     * */
    @GetMapping("query")
    public ResponseEntity<User> queryUser(Integer id) {
        User user = userService.queryUserByEmail(id);
        if (user == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(user);
    }

    /*
     * 根据email获取用户信息
     * */
    @GetMapping("query")
    public ResponseEntity<User> queryUser(String email) {
        User user = userService.queryUserByEmail(email);
        if (user == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(user);
    }


}// end
