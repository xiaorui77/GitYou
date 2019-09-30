package com.gityou.user.controller;


import com.gityou.common.entity.RequestResult;
import com.gityou.common.entity.UserInfo;
import com.gityou.common.utils.CookieUtils;
import com.gityou.user.config.AuthProperties;
import com.gityou.user.pojo.User;
import com.gityou.user.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    private AuthProperties authProperties;

    /*
     *  验证
     * */
    public ResponseEntity<RequestResult<UserInfo>> authentication(String username, String password,
                                                                  HttpServletRequest request, HttpServletResponse response) {
        RequestResult<User> result = authService.authentication(username, password);

        if (result.getCode() == 200) {
            CookieUtils.setCookie(request, response, authProperties.getCookieName(), result.getMsg(), authProperties.getExpire() * 3600);
            UserInfo info = new UserInfo(result.getData().getId(), result.getData().getUsername());
            return ResponseEntity.ok(new RequestResult<>(result.getCode(), "验证成功", info));
        } else
            return ResponseEntity.status(result.getCode()).body(new RequestResult<>(result.getCode(), result.getMsg()));
    }


}// end
