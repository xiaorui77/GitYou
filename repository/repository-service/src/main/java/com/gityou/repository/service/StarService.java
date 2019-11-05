package com.gityou.repository.service;


import com.gityou.common.entity.UserInfo;
import com.gityou.repository.interceptor.LoginInterceptor;
import com.gityou.repository.mapper.StarMapper;
import com.gityou.repository.pojo.Star;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StarService {
    @Autowired
    private StarMapper starMapper;

    // 星标
    public Boolean star(Star star) {
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        if (loginUser == null)
            return null;

        star.setId(loginUser.getId());
        star.setStarTime((int) (System.currentTimeMillis() / 1000));

        return starMapper.updateByPrimaryKey(star) == 1;
    }

}
