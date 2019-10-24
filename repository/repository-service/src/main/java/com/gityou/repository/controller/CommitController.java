package com.gityou.repository.controller;

import com.gityou.common.entity.PageResult;
import com.gityou.repository.entity.CommitResult;
import com.gityou.repository.entity.DiffResult;
import com.gityou.repository.entity.FileDiffResult;
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

    /*
     * 根据提交id获取提交信息
     * */
    @GetMapping
    public ResponseEntity<CommitResult> query(String user, String name, String commit) {
        CommitResult result = commitService.query(user, name, commit);
        if (result == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }


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
     * 获取提交列表
     */
    @GetMapping("list")
    public ResponseEntity<PageResult> commitList(
            String user, String name, String branch,
            @RequestParam(defaultValue = "") String author,
            @RequestParam(defaultValue = "1") Integer page) {
        PageResult<CommitResult> result = commitService.commitList(user, name, branch, author, page);
        if (result == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }

    /*
     * 某次提交修改的文件列表
     * */
    @GetMapping("change")
    public ResponseEntity<List> changeList(String user, String name, String commit) {
        List<FileDiffResult> result = commitService.changeList(user, name, commit);
        if (result == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }


    /*
     * 文件差异 单个文件
     * */
    @GetMapping("diff")
    public ResponseEntity<DiffResult> diff(String user, String name, String commit, String path) {
        DiffResult result = commitService.diff(user, name, commit, path);
        if (result == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }

}/// end
