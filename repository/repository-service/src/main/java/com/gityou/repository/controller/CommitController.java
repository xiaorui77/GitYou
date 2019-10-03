package com.gityou.repository.controller;

import com.gityou.repository.entity.CommitResult;
import com.gityou.repository.service.CommitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("commit")
public class CommitController {
    @Autowired
    private CommitService commitService;

    /**
     * 获取最近一次提交 简单格式
     */
    @GetMapping("last")
    public ResponseEntity<CommitResult> lastCommit(String user, String name) {
        CommitResult result = commitService.lastCommit(user, name);
        if (result == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }


}/// end
