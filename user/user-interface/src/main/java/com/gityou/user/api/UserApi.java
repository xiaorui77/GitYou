package com.gityou.user.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.Set;

public interface UserApi {
    @PostMapping("/user/queryUsersByEmail")
    Map<String, String> queryUsersByEmail(@RequestBody Set<String> emails);
}
