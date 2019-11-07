package com.gityou.repository.service;


import com.gityou.common.entity.UserInfo;
import com.gityou.repository.interceptor.LoginInterceptor;
import com.gityou.repository.mapper.RepositoryMapper;
import com.gityou.repository.mapper.StarMapper;
import com.gityou.repository.pojo.Star;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

@Service
public class StarService {
    @Autowired
    private StarMapper starMapper;

    @Autowired
    private RepositoryMapper repositoryMapper;

    public Star query(Integer user, Long repository) {
        Star record = new Star();
        record.setUser(user);
        record.setRepository(repository);

        Star result = starMapper.selectOne(record);
        if (result == null)
            return record;
        else
            return result;
    }

    // watch todo: 还需要通知服务, 待完成
    public Boolean watch(Star watch) {
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        if (loginUser == null || !loginUser.getId().equals(watch.getUser()))
            return null;

        Star record = queryAttention(watch.getUser(), watch.getRepository(), "watch");
        if (record == null)
            return false;

        // 如果已经是结果, 则不用更新
        if (record.getStar() == watch.getStar())
            return true;

        // 更新 attention表的watch
        watch.setStarTime((int) (System.currentTimeMillis() / 1000));
        if (starMapper.updateByPrimaryKeySelective(watch) != 1)
            return false;

        // 判断是否要更新repository
        if (watch.getWatch() > 0 && record.getWatch() > 0)
            return true;

        return repositoryMapper.increase(watch.getRepository(), "watch", watch.getWatch() > 0 ? 1 : -1) == 1;
    }

    // 星标
    @Transactional
    public Boolean star(Star star) {
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        if (loginUser == null || !loginUser.getId().equals(star.getUser()))
            return null;

        Star record = queryAttention(star.getUser(), star.getRepository(), "star");
        if (record == null)
            return false;

        // 如果已经是结果, 则不用更新
        if (record.getStar() == star.getStar())
            return true;

        // 更新 attention表的star
        star.setStarTime((int) (System.currentTimeMillis() / 1000));
        if (starMapper.updateByPrimaryKeySelective(star) != 1)
            return false;

        return repositoryMapper.increase(star.getRepository(), "star", star.getStar() ? 1 : -1) == 1;
    }

    // fork
    @Transactional
    public Boolean fork(Star fork) {
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        if (loginUser == null || !loginUser.getId().equals(fork.getUser()))
            return null;

        Star record = queryAttention(fork.getUser(), fork.getRepository(), "fork");
        if (record == null)
            return false;

        // 如果已经是结果, 则不用更新
        if (record.getStar() == fork.getStar())
            return true;

        // 更新 attention表的star
        fork.setStarTime((int) (System.currentTimeMillis() / 1000));
        if (starMapper.updateByPrimaryKeySelective(fork) != 1)
            return false;

        return repositoryMapper.increase(fork.getRepository(), "star", fork.getStar() ? 1 : -1) == 1;
    }

    private Star queryAttention(Integer user, Long repository, String field) {
        Example example = new Example(Star.class);
        Example.Criteria criteria = example.createCriteria();
        example.selectProperties(field);
        criteria.andEqualTo("user", user);
        criteria.andEqualTo("repository", repository);
        return starMapper.selectOneByExample(example);
    }

}// end
