package com.gityou.user.controller;


import com.gityou.common.entity.RequestResult;
import com.gityou.common.entity.UserInfo;
import com.gityou.common.utils.CookieUtils;
import com.gityou.common.utils.JwtUtils;
import com.gityou.user.config.AuthProperties;
import com.gityou.common.pojo.User;
import com.gityou.user.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@EnableConfigurationProperties(AuthProperties.class)
@RequestMapping("auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    private AuthProperties authProperties;


    /*
     * 登录
     * */
    @PostMapping("login")
    public ResponseEntity<RequestResult<UserInfo>> login(String username, String password,
                                                         HttpServletRequest request, HttpServletResponse response) {
        RequestResult<User> result = authService.authentication(username, password);

        if (result.getCode() == 200) {
            CookieUtils.setCookie(request, response, authProperties.getCookieName(), result.getMsg(), authProperties.getExpire() * 3600);
            UserInfo info = new UserInfo(result.getData().getId(), result.getData().getUsername());
            return ResponseEntity.ok(new RequestResult<>(result.getCode(), "登录成功", info));
        } else
            return ResponseEntity.status(result.getCode()).body(new RequestResult<>(result.getCode(), result.getMsg()));
    }

    /*
     * 退出登录
     * */
    @PostMapping("logout")
    public ResponseEntity<RequestResult<UserInfo>> logout(String username, String password,
                                                          HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, authProperties.getCookieName());
        return ResponseEntity.ok(RequestResult.ok());
    }

    /*
     * 验证
     * */
    @PostMapping("verify")
    public ResponseEntity<RequestResult<UserInfo>> verify(@CookieValue("user-identity") String token,
                                                          HttpServletRequest request, HttpServletResponse response) {
        try {
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, authProperties.getPublicKey());
            return ResponseEntity.ok(new RequestResult<>(200, "验证通过", userInfo));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}// end
