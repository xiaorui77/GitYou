package com.gityou.repository.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gityou.common.entity.PageResult;
import com.gityou.common.entity.RequestResult;
import com.gityou.common.entity.UserInfo;
import com.gityou.repository.Utils.GitUtils;
import com.gityou.repository.interceptor.LoginInterceptor;
import com.gityou.repository.mapper.RepositoryMapper;
import com.gityou.repository.pojo.Repository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RepositoryService {
    private final RepositoryMapper repositoryMapper;
    private final GitUtils gitUtils;

    public RepositoryService(RepositoryMapper repositoryMapper, GitUtils gitUtils) {
        this.repositoryMapper = repositoryMapper;
        this.gitUtils = gitUtils;
    }


    // 分页查询
    public PageResult<Repository> queryRepos(String user, Integer page, Integer type, Integer language, String keyword) {
        UserInfo userInfo = LoginInterceptor.getLoginUser();

        // 分页
        PageHelper.startPage(page, 20);

        Example example = new Example(Repository.class);
        Example.Criteria criteria = example.createCriteria();

        // 查找user
        criteria.andEqualTo("username", user);

        // type public private过滤
        if (type == 1)
            criteria.andEqualTo("secret", 0);
        else if (type == 2 && userInfo != null && Objects.equals(userInfo.getUsername(), user))
            criteria.andEqualTo("secret", 1);

        if (type > 2)
            criteria.andEqualTo("type", type);

        // language过滤
        if (language != 0)
            criteria.andEqualTo("language", language);

        if (StringUtils.isNotBlank(keyword))
            criteria.andEqualTo("name", "%" + keyword + "%");

        List<Repository> repositories = repositoryMapper.selectByExample(example);
        PageInfo<Repository> result = new PageInfo<>(repositories);

        return new PageResult<Repository>(result.getTotal(), result.getPageNum(), repositories);
    }

    // 流行
    public List<Repository> queryPopularRepos(Integer id) {
        // Todo
        return new ArrayList<>();
    }

    // 详情
    Repository queryRepository() {
        // Todo
        return new Repository();
    }

    // 创建仓库
    public RequestResult createRepository(Repository repository) {
        // 验证登录
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        if (loginUser == null)
            return RequestResult.build(401, "用户未登录");

        Integer userId = loginUser.getId();
        if (!userId.equals(repository.getUserId()))
            return RequestResult.build(401, "登录的用户不一致");

        // 判读name不能为空
        if (StringUtils.isBlank(repository.getName()) || repository.getName().length() > 50)
            return RequestResult.build(401, "仓库名不合法");

        // name是否可用
        if (hasName(repository.getName()))
            return RequestResult.build(401, "仓库已存在");


        repository.setUsername(loginUser.getUsername());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        repository.setCreateTime(timestamp);
        repository.setUpdateTime(timestamp);

        if (gitUtils.createNewRepository(repository.getUsername(), repository.getName())) {
            repositoryMapper.insertSelective(repository);
            return RequestResult.ok(repository);
        } else {
            return RequestResult.build(400, "创建失败");
        }

    }

    // 导入仓库
    public RequestResult importRepository(Repository repository, String clone) {
        // 验证登录
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        if (loginUser == null)
            return RequestResult.build(401, "用户未登录");

        Integer userId = loginUser.getId();
        if (!userId.equals(repository.getUserId()))
            return RequestResult.build(401, "登录的用户不一致");

        // 判读name不能为空
        if (StringUtils.isBlank(repository.getName()) || repository.getName().length() > 50)
            return RequestResult.build(401, "仓库名不合法");

        // name是否可用
        if (hasName(repository.getName()))
            return RequestResult.build(401, "仓库已存在");


        repository.setUsername(loginUser.getUsername());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        repository.setCreateTime(timestamp);
        repository.setUpdateTime(timestamp);

        if (gitUtils.cloneRepository(repository.getUsername(), repository.getName(), clone)) {
            repositoryMapper.insertSelective(repository);
            return RequestResult.ok(repository);
        } else {
            return RequestResult.build(400, "导入仓库失败");
        }

    }


    // 查询Repository 基本信息
    public Repository queryRepositoryByName(String user, String name) {
        Example example = new Example(Repository.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", user);
        criteria.andEqualTo("name", name);
        return repositoryMapper.selectOneByExample(example);
    }

    // 是否已经存在Repository
    public Boolean hasName(String name) {
        return name.equals(repositoryMapper.hasName(name));
    }

}// end
