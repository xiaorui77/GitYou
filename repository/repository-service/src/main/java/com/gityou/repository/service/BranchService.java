package com.gityou.repository.service;


import com.gityou.repository.Utils.GitUtils;
import com.gityou.repository.entity.BranchResult;
import com.gityou.repository.mapper.BranchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchService {
    @Autowired
    private BranchMapper branchMapper;

    @Autowired
    private GitUtils gitUtils;


    // 获取branch列表
    public List<BranchResult> queryList(String user, String name) {
        return gitUtils.branchList(user, name);
    }

}// end
