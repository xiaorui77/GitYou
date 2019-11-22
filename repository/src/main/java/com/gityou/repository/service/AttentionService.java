package com.gityou.repository.service;


import com.gityou.common.entity.UserInfo;
import com.gityou.common.pojo.Attention;
import com.gityou.common.pojo.SubscriptionRepository;
import com.gityou.common.pojo.User;
import com.gityou.repository.client.UserClient;
import com.gityou.repository.interceptor.LoginInterceptor;
import com.gityou.repository.mapper.AttentionMapper;
import com.gityou.repository.mapper.RepositoryMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class AttentionService {
    @Autowired
    private AttentionMapper attentionMapper;

    @Autowired
    private RepositoryMapper repositoryMapper;

    @Autowired
    private UserClient userClient;

    @Autowired
    private RabbitTemplate amqp;


    // 用户的attention信息
    public Attention query(Integer user, Long repository) {
        Attention record = new Attention();
        record.setUser(user);
        record.setRepository(repository);

        Attention result = attentionMapper.selectOne(record);
        if (result == null)
            return record;
        else
            return result;
    }

    // watch 该仓库的人
    public List queryWatch(Long repositoryId) {
        Example example = new Example(Attention.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("repository", repositoryId);
        criteria.andNotEqualTo("watch", 0);
        List<Attention> attentionList = attentionMapper.selectByExample(example);

        // 查询用户信息并返回
        return queryUserInfo(attentionList);
    }

    // star 该仓库的人
    public List queryStar(Long repositoryId) {
        Example example = new Example(Attention.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("repository", repositoryId);
        criteria.andNotEqualTo("star", true);
        List<Attention> attentionList = attentionMapper.selectByExample(example);

        // 查询用户信息
        return queryUserInfo(attentionList);
    }

    // fork 该仓库的人
    public List queryFork(Long repositoryId) {
        Example example = new Example(Attention.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("repository", repositoryId);
        criteria.andNotEqualTo("fork", true);
        List<Attention> attentionList = attentionMapper.selectByExample(example);

        // 查询用户信息
        return queryUserInfo(attentionList);
    }

    // watch todo: 还需要通知服务, 待完成
    public Boolean watch(Attention watch) {
        // 验证用户
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        if (loginUser == null || !loginUser.getId().equals(watch.getUser()))
            return null;

        // 查询attention信息
        Attention record = queryAttention(watch.getUser(), watch.getRepository(), "watch");
        if (record == null)
            return false;

        // 如果已经是结果, 则不用更新
        if (watch.getWatch().equals(record.getWatch()))
            return true;

        // 更新 attention表的watch
        watch.setWatchTime((int) (System.currentTimeMillis() / 1000));
        if (attentionMapper.updateByPrimaryKeySelective(watch) != 1)
            return false;

        // 发送到消息队列, 以创建订阅
        SubscriptionRepository subscription = new SubscriptionRepository();
        subscription.setUser(loginUser.getId());
        subscription.setRepository(watch.getRepository());
        subscription.setType(watch.getWatch());
        if (record.getWatch() == 0)
            amqp.convertAndSend("subscription.service", "watch.create", subscription);
        else
            amqp.convertAndSend("subscription.service", "watch.update", subscription);

        // 判断是否要更新repository
        if (watch.getWatch() > 0 && record.getWatch() > 0)
            return true;

        return repositoryMapper.increase(watch.getRepository(), "watch", watch.getWatch() > 0 ? 1 : -1) == 1;
    }

    // 星标
    @Transactional
    public Boolean star(Attention star) {
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        if (loginUser == null || !loginUser.getId().equals(star.getUser()))
            return null;

        Attention record = queryAttention(star.getUser(), star.getRepository(), "star");
        if (record == null)
            return false;

        // 如果已经是结果, 则不用更新
        if (record.getStar() == star.getStar())
            return true;

        // 更新 attention表的star
        star.setStarTime((int) (System.currentTimeMillis() / 1000));
        if (attentionMapper.updateByPrimaryKeySelective(star) != 1)
            return false;

        return repositoryMapper.increase(star.getRepository(), "star", star.getStar() ? 1 : -1) == 1;
    }

    // fork todo:
    @Transactional
    public Boolean fork(Attention fork) {
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        if (loginUser == null || !loginUser.getId().equals(fork.getUser()))
            return null;

        Attention record = queryAttention(fork.getUser(), fork.getRepository(), "fork");
        if (record == null)
            return false;

        // 如果已经是结果, 则不用更新
        if (record.getStar() == fork.getStar())
            return true;

        // 更新 attention表的star
        fork.setStarTime((int) (System.currentTimeMillis() / 1000));
        if (attentionMapper.updateByPrimaryKeySelective(fork) != 1)
            return false;

        return repositoryMapper.increase(fork.getRepository(), "star", fork.getStar() ? 1 : -1) == 1;
    }

    private Attention queryAttention(Integer user, Long repository, String field) {
        Example example = new Example(Attention.class);
        Example.Criteria criteria = example.createCriteria();
        example.selectProperties(field);
        criteria.andEqualTo("user", user);
        criteria.andEqualTo("repository", repository);
        return attentionMapper.selectOneByExample(example);
    }

    // 查询用户信息, username和avatar
    private List queryUserInfo(List<Attention> attentionList) {
        Set<Integer> userIds = new HashSet<>(attentionList.size());
        for (Attention a : attentionList)
            userIds.add(a.getUser());

        Map<Integer, User> users = userClient.queryUsers(userIds);
        for (Attention a : attentionList) {
            a.setUsername(users.get(a.getUser()).getUsername());
            a.setAvatar(users.get(a.getUser()).getAvatar());
        }
        return attentionList;
    }

}// end
