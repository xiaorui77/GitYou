package com.gityou.repository.service;


import com.gityou.repository.mapper.BranchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BranchService {
    @Autowired
    private BranchMapper branchMapper;
}
