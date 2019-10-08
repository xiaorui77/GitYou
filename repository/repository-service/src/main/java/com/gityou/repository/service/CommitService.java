package com.gityou.repository.service;

import com.gityou.repository.utils.GitUtils;
import com.gityou.repository.client.UserClient;
import com.gityou.repository.entity.CommitResult;
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


    // 获取最近一次提交
    public CommitResult lastCommit(String user, String name) {
        return gitUtils.lastCommit(user, name);
    }

    // commit列表
    public List<CommitResult> commitList(String user, String name, String branch, Integer page) {
        List<CommitResult> commits = gitUtils.commitList(user, name, branch, page);

        // 获取用户名
        Set<String> emails = new HashSet<>();
        commits.forEach(e -> emails.add(e.getEmail()));

        Map<String, String> users = userClient.queryUsersByEmail(emails);
        commits.forEach(e -> e.setEmail(users.get(e.getEmail())));
        return commits;
    }


}// end
