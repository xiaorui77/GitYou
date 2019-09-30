package com.gityou.repository.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gityou.common.entity.PageResult;
import com.gityou.common.entity.RequestResult;
import com.gityou.common.entity.UserInfo;
import com.gityou.repository.interceptor.LoginInterceptor;
import com.gityou.repository.mapper.RepositoryMapper;
import com.gityou.repository.pojo.Repository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RepositoryService {
    private final RepositoryMapper repositoryMapper;

    public RepositoryService(RepositoryMapper repositoryMapper) {
        this.repositoryMapper = repositoryMapper;
    }

    // 分页查询
    public PageResult<Repository> queryRepos(Integer userId, Integer page, Integer type, Integer language, String keyword) {
        UserInfo userInfo = LoginInterceptor.getLoginUser();

        // 分页
        PageHelper.startPage(page, 20);

        Example example = new Example(Repository.class);
        Example.Criteria criteria = example.createCriteria();

        // 查找user
        criteria.andEqualTo("userId", userId);

        // type public private过滤
        if (type == 1)
            criteria.andEqualTo("secret", 0);
        else if (type == 2 && userInfo != null && Objects.equals(userInfo.getId(), userId))
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

        repositoryMapper.insertSelective(repository);
        return RequestResult.ok();
    }

}// end
