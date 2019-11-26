package com.gityou.repository.controller;


import com.gityou.common.entity.BranchResult;
import com.gityou.common.pojo.Branch;
import com.gityou.repository.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("branch")
public class BranchController {

    @Autowired
    private BranchService branchService;

    /**
     * 根据用户名,仓库名 查询branch
     */
    @GetMapping("list")
    public ResponseEntity<List<BranchResult>> queryList(String user, String name) {
        List<BranchResult> result = branchService.queryList(user, name);
        if (result == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }


    /**
     * 根据仓库id 查询branch
     */
    @GetMapping("all")
    public ResponseEntity<List<Branch>> queryList(Long repository) {
        List result = branchService.queryList(repository);
        if (result == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }

}// end
