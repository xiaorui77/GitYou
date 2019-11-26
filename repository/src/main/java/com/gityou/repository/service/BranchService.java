package com.gityou.repository.service;


import com.gityou.common.entity.BranchResult;
import com.gityou.common.pojo.Branch;
import com.gityou.repository.client.UserClient;
import com.gityou.repository.mapper.BranchMapper;
import com.gityou.repository.utils.GitUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class BranchService {
    @Autowired
    private BranchMapper branchMapper;

    @Autowired
    private GitUtils gitUtils;

    @Autowired
    private UserClient userClient;


    // 获取branch列表
    public List<BranchResult> queryList(String user, String name) {
        List<BranchResult> branches = gitUtils.branchList(user, name);

        Set<String> emails = new HashSet<>();
        branches.forEach(e -> emails.add(e.getAuthor()));

        Map<String, String> users = userClient.queryUsersByEmail(emails);

        // 将结果中user的email改为username
        branches.forEach(e -> e.setAuthor(users.get(e.getAuthor())));
        return branches;
    }

    // 获取branch列表 从数据库中
    public List<Branch> queryList(Long repositoryId) {
        Example example = new Example(Branch.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("repository", repositoryId);

        return branchMapper.selectByExample(example);
    }

}// end
