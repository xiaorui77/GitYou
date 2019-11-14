package com.gityou.repository.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gityou.common.entity.PageResult;
import com.gityou.repository.client.UserClient;
import com.gityou.repository.mapper.IssueCommentMapper;
import com.gityou.repository.mapper.IssueMapper;
import com.gityou.repository.mapper.RepositoryMapper;
import com.gityou.common.pojo.Issue;
import com.gityou.common.pojo.IssueComment;
import com.gityou.common.pojo.Repository;
import com.gityou.common.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class IssueService {
    @Autowired
    private UserClient userClient;

    @Autowired
    private IssueMapper issueMapper;

    @Autowired
    private RepositoryMapper repositoryMapper;

    @Autowired
    private IssueCommentMapper issueCommentMapper;

    // 根据repository获取
    public PageResult<Issue> issuePage(String user, String repository, Integer page) {
        Long repoId = repositoryMapper.queryId(user, repository);
        if (repoId == null)
            return null;

        Example example = new Example(Issue.class);
        example.setOrderByClause("create_time DESC");
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("repository", repoId);
        // 分页 注意: 只对后续的第一条sql语句有用
        PageHelper.startPage(page, 4);
        List<Issue> issueList = issueMapper.selectByExample(example);
        PageInfo<Issue> result = new PageInfo<>(issueList);

        // 获取username
        Set<Integer> users = new HashSet<>();
        result.getList().forEach(e -> users.add(e.getAuthorId()));
        Map<Integer, String> names = userClient.queryNames(users);
        result.getList().forEach(e -> e.setAuthorName(names.get(e.getAuthorId())));

        return new PageResult<Issue>(result.getTotal(), result.getPageNum(), issueList);
    }

    // 获取issue信息
    public Issue issue(String username, String repository, Integer number) {
        Long repoId = repositoryMapper.queryId(username, repository);
        if (repoId == null)
            return null;

        Example example = new Example(Issue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("repository", repoId);
        criteria.andEqualTo("number", number);
        Issue record = issueMapper.selectOneByExample(example);

        // 获取username
        User user = userClient.queryUser(record.getAuthorId());
        record.setAuthorName(user.getUsername());
        record.setAuthorAvatar(user.getAvatar());
        return record;
    }

    // 获取issue comment列表
    public List<IssueComment> issueComments(String user, String repository, Integer number) {
        // 获取仓库id
        Long repoId = repositoryMapper.queryId(user, repository);
        if (repoId == null)
            return null;

        // 获取issue id
        Long issueId = issueMapper.queryId(repoId, number);
        if (issueId == null)
            return null;

        return issueComments(issueId);
    }

    // 根据Id获取Issue_Comment列表
    public List<IssueComment> issueComments(Long issueId) {
        Example example = new Example(IssueComment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("issue", issueId);
        List<IssueComment> comments = issueCommentMapper.selectByExample(example);

        // 获取用户信息
        Set<Integer> names = new HashSet<>();
        comments.forEach(e -> names.add(e.getAuthorId()));
        Map<Integer, User> users = userClient.queryUsers(names);

        comments.forEach(e -> {
            User u = users.get(e.getAuthorId());
            e.setAuthorName(u.getUsername());
            e.setAuthorAvatar(u.getAvatar());
        });
        return comments;
    }


    // 创建Issue
    public Boolean issueCreate(Issue issue) {
        Example example = new Example(Repository.class);
        Example.Criteria criteria = example.createCriteria();
        example.selectProperties("issueNum");
        criteria.andEqualTo("id", issue.getRepository());

        int issueNum = repositoryMapper.selectOneByExample(example).getIssueNum();
        issue.setNumber(issueNum);
        issue.setCreateTime((int) (System.currentTimeMillis() / 1000));

        if (issueMapper.insertSelective(issue) == 1)
            return repositoryMapper.increase(issue.getRepository(), "issue_num", 1) == 1;
        return false;
    }

}// end
