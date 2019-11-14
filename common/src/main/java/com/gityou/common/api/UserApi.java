package com.gityou.common.api;

import com.gityou.common.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;

public interface UserApi {

    @GetMapping("user")
    User queryUser(@RequestParam Integer id);

    @GetMapping("user/byUsername")
    User queryUserByUsername(@RequestParam String username);

    @GetMapping("user/byEmail")
    User queryUserByEmail(@RequestParam String email);


    @PostMapping("user/queryUsersByEmail")
    Map<String, String> queryUsersByEmail(@RequestBody Set<String> emails);

    @PostMapping("user/queryNames")
    Map<Integer, String> queryNames(@RequestBody Set<Integer> ids);

    @PostMapping("user/users")
    Map<Integer, User> queryUsers(@RequestBody Set<Integer> ids);
}
