package com.gityou.user.controller;

import com.gityou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/*
 * User Controller
 * */
@RestController
public class UserController {
    @Autowired
    UserService userService;

}
