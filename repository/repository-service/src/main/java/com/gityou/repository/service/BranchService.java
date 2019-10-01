package com.gityou.repository.service;


import com.gityou.repository.mapper.BranchMapper;
import com.gityou.repository.pojo.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchService {
    @Autowired
    private BranchMapper branchMapper;

    public List<Branch> queryList(Long id) {
        Branch record = new Branch();
        record.setRepoId(id);
        return branchMapper.select(record);
    }

}// end
