package com.gityou.repository.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gityou.common.entity.PageResult;
import com.gityou.common.entity.ResponseResult;
import com.gityou.common.entity.UserInfo;
import com.gityou.common.pojo.Repository;
import com.gityou.common.utils.JsonUtils;
import com.gityou.repository.interceptor.LoginInterceptor;
import com.gityou.repository.mapper.RepositoryMapper;
import com.gityou.repository.utils.GitUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RabbitTemplate rabbitTemplate;

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

    // 某用户的首页展示仓库
    public List<Repository> queryPopularRepos(String username, int num) {
        Example example = new Example(Repository.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        example.setOrderByClause("score desc");
        return repositoryMapper.selectByExampleAndRowBounds(example, new RowBounds(0, num));
    }

    // 根据id 查询Repository 基本信息
    public Repository queryRepository(Long repositoryId) {
        return repositoryMapper.selectByPrimaryKey(repositoryId);
    }

    // 根据用户名,仓库名 查询Repository 基本信息
    public Repository queryRepositoryByName(String user, String name) {
        Example example = new Example(Repository.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", user);
        criteria.andEqualTo("name", name);
        return repositoryMapper.selectOneByExample(example);
    }

    // 创建仓库
    public ResponseResult createRepository(Repository repository) {
        // 验证登录, 仓库名是否可用, 设置repository信息
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        ResponseResult verify = verify(loginUser, repository);
        if (verify != null)
            return verify;

        // 计算存放位置
        List<Integer> location = storageLocation();
        repository.setMachine(Long.valueOf(location.get(0)));

        // 插入数据库
        if (repositoryMapper.insertSelective(repository) != 1)
            return ResponseResult.fail("创建失败, 未知错误!");

        // 发送创建消息 MQ
        for (int i = 1; i < location.size(); i++)
            rabbitTemplate.convertAndSend("git.service", "new." + location.get(i), JsonUtils.serialize(repository));

        return ResponseResult.ok(repository);
    }

    // 导入仓库
    public ResponseResult importRepository(Repository repository) {
        // 验证登录, 仓库名是否可用, 设置repository信息
        UserInfo loginUser = LoginInterceptor.getLoginUser();
        ResponseResult verify = verify(loginUser, repository);
        if (verify != null)
            return verify;

        // 计算存放位置
        List<Integer> location = storageLocation();
        repository.setMachine(Long.valueOf(location.get(0)));

        // 插入数据库
        if (repository.getImportFrom() == null)
            return ResponseResult.fail("导入失败, 没有指定要导入的url");
        if (repositoryMapper.insertSelective(repository) != 1)
            return ResponseResult.fail("创建失败, 未知错误!");

        // 发送创建消息 MQ
        for (int i = 1; i < location.size(); i++)
            rabbitTemplate.convertAndSend("git.service", "import." + location.get(i), JsonUtils.serialize(repository));

        return ResponseResult.ok(repository);
    }


    // 验证name是否合法
    private ResponseResult verify(UserInfo loginUser, Repository repository) {
        // 验证登录
        if (loginUser == null)
            return ResponseResult.build(401, "用户未登录");

        Integer userId = loginUser.getId();
        if (!userId.equals(repository.getUserId()))
            return ResponseResult.build(401, "登录的用户不一致");

        // 判读name不能为空
        if (StringUtils.isBlank(repository.getName()) || repository.getName().length() > 50)
            return ResponseResult.fail("仓库名不合法");

        // name是否可用
        if (hasName(repository.getName()))
            return ResponseResult.fail("仓库已存在");


        // 设置仓库信息
        repository.setUsername(loginUser.getUsername());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        repository.setCreateTime(timestamp);
        repository.setUpdateTime(timestamp);

        return null;
    }

    // 是否已经存在Repository
    private Boolean hasName(String name) {
        return name.equals(repositoryMapper.hasName(name));
    }

    // 计算存放位置
    private List<Integer> storageLocation() {
        List<Integer> location = new ArrayList<>(4);

        int[] all = {1, 2};
        // 假设 为1,2号
        location.add(0);

        int a = 0;
        for (int i = 0; i < 2; i++) {
            a |= all[i] << 10 * i;
            location.add(all[i]);
        }
        location.set(0, a);

        return location;
    }

}// end
