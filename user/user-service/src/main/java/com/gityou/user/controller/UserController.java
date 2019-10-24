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
        User user = userService.queryUser(id);
        if (user == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(user);
    }

    /*
     * 根据username 获取用户信息
     * */
    @RequestMapping("byUsername")
    public ResponseEntity<User> queryUserByUsername(@RequestParam String username) {
        User user = userService.queryByUsername(username);
        if (user == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(user);
    }

    /*
     * 根据email获取用户信息
     * */
    @RequestMapping("byEmail")
    public ResponseEntity<User> queryUserByEmail(@RequestParam String email) {
        User user = userService.queryByEmail(email);
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
