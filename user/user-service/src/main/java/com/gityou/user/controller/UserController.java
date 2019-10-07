package com.gityou.user.controller;

import com.gityou.user.pojo.User;
import com.gityou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

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
    @GetMapping
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

    @PostMapping("queryUsersByEmail")
    public ResponseEntity<Map<String, String>> queryUsersByEmail(@RequestBody Set<String> emails) {
        Map<String, String> users = userService.queryUsersByEmail(emails);
        if (users == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(users);
    }


}// end
