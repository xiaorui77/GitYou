package com.gityou.repository.service;

import com.gityou.common.entity.PageResult;
import com.gityou.repository.client.UserClient;
import com.gityou.repository.entity.CommitResult;
import com.gityou.repository.entity.DiffResult;
import com.gityou.repository.entity.FileDiffResult;
import com.gityou.repository.utils.GitUtils;
import com.gityou.user.pojo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
public class CommitService {

    @Autowired
    private GitUtils gitUtils;

    @Autowired
    private UserClient userClient;

    // 根据commit id查询Commit信息
    public CommitResult query(String username, String name, String commit) {
        CommitResult result = gitUtils.query(username, name, commit);
        User author = userClient.queryUserByEmail(result.getEmail());
        result.setAuthor(author.getUsername());
        return result;
    }

    // 获取最近一次提交
    public CommitResult lastCommit(String user, String name) {
        return gitUtils.lastCommit(user, name);
    }

    // commit列表
    public PageResult<CommitResult> commitList(String user, String name, String branch, String author, Integer page) {
        String email = null;
        if (StringUtils.isNotEmpty(author))
            email = userClient.queryUserByUsername(author).getEmail();

        PageResult<CommitResult> result = gitUtils.commitList(user, name, branch, email, page);
        List<CommitResult> commits = result.getList();

        // 获取用户名
        Set<String> emails = new HashSet<>();
        commits.forEach(e -> emails.add(e.getEmail()));

        Map<String, String> users = userClient.queryUsersByEmail(emails);
        commits.forEach(e -> e.setEmail(users.get(e.getEmail())));
        return result;
    }

    // 文件修改列表
    public List<FileDiffResult> changeList(String user, String name, String commit) {
        return gitUtils.changeList(user, name, commit);
    }

    // 文件差异
    public DiffResult diff(String user, String name, String commit, String path) {
        return gitUtils.diff(user, name, commit, path);
    }


}// end
