package com.gityou.user.api;

import com.gityou.user.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Set;

public interface UserApi {

    @GetMapping(value = "byEmail")
    User queryUserByEmail(@RequestParam String email);


    @PostMapping("user/queryUsersByEmail")
    Map<String, String> queryUsersByEmail(@RequestBody Set<String> emails);
}
