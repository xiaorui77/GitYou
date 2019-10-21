package com.gityou.repository.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gityou.common.entity.PageResult;
import com.gityou.repository.mapper.IssueMapper;
import com.gityou.repository.mapper.RepositoryMapper;
import com.gityou.repository.pojo.Issue;
import com.gityou.repository.pojo.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class IssueService {
    @Autowired
    private IssueMapper issueMapper;

    @Autowired
    private RepositoryMapper repositoryMapper;

    // 根据repository获取
    public PageResult<Issue> issuePage(String repository, Integer page) {
        Repository repo = new Repository();
        repo.setName(repository);
        repo = repositoryMapper.selectOne(repo);

        if (repo == null)
            return null;

        Example example = new Example(Issue.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("repository", repo.getId());
        // 分页 只对后续的一条有用
        PageHelper.startPage(page, 4);
        List<Issue> issueList = issueMapper.selectByExample(example);
        PageInfo<Issue> result = new PageInfo<>(issueList);

        return new PageResult<Issue>(result.getTotal(), result.getPageNum(), issueList);
    }
}// end
