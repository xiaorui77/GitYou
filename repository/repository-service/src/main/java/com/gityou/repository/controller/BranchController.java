package com.gityou.repository.controller;


import com.gityou.repository.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BranchController {

    @Autowired
    private BranchService branchService;

    /* 根据仓库查询branch
     * */
    @GetMapping("list")
    public void queryList() {

    }

}// end
