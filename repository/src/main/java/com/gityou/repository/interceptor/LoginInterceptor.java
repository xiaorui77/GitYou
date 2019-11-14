package com.gityou.repository.interceptor;

import com.gityou.common.entity.UserInfo;
import com.gityou.common.utils.CookieUtils;
import com.gityou.common.utils.JwtUtils;
import com.gityou.repository.config.JwtProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    private JwtProperties properties;

    private static final ThreadLocal<UserInfo> tl = new ThreadLocal<>();

    public LoginInterceptor(JwtProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 查询token
        String token = CookieUtils.getCookieValue(request, properties.getCookieName());
        if (StringUtils.isBlank(token)) {
            // 未登录, 允许未登录
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return true;
        }
        // 有token，查询用户信息
        try {
            // 解析成功，证明已经登录
            UserInfo user = JwtUtils.getInfoFromToken(token, properties.getPublicKey());
            // 放入线程域
            tl.set(user);
            return true;
        } catch (Exception e) {
            // 抛出异常，证明未登录, 继续
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return true;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        tl.remove();
    }

    public static UserInfo getLoginUser() {
        return tl.get();
    }

}
