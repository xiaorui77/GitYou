package com.gityou.repository.controller;

import com.gityou.repository.entity.ChangeResult;
import com.gityou.repository.entity.CommitResult;
import com.gityou.repository.service.CommitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


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

    /**
     * 获取提交
     */
    @GetMapping("list")
    public ResponseEntity<List> commitList(String user, String name,
                                           @RequestParam(defaultValue = "master") String branch,
                                           @RequestParam(defaultValue = "1") Integer page) {
        List result = commitService.commitList(user, name, branch, page);
        if (result == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }

    /*
     * 某次提交修改的文件列表
     * */
    @GetMapping("change")
    public ResponseEntity changeList(String user, String name, String commit) {
        List<ChangeResult> result = commitService.changeList(user, name, commit);
        if (result == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }

}/// end
