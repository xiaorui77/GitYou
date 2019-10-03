package com.gityou.repository.service;

import com.gityou.repository.Utils.GitUtils;
import com.gityou.repository.entity.CommitResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CommitService {

    @Autowired
    private GitUtils gitUtils;


    // 获取最近一次提交
    public CommitResult lastCommit(String user, String name) {
        return gitUtils.lastCommit(user, name);
    }

}// end
