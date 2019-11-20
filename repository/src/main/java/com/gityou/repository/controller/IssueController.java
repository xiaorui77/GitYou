package com.gityou.repository.controller;

import com.gityou.common.entity.PageResult;
import com.gityou.common.entity.ResponseResult;
import com.gityou.common.pojo.Issue;
import com.gityou.common.pojo.IssueComment;
import com.gityou.repository.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("issue")
public class IssueController {
    @Autowired
    private IssueService issueService;


    /*
     * 根据仓库id获取issue列表
     * */
    @GetMapping("page")
    public ResponseEntity<PageResult<Issue>> issuePage(String user, String repository, @RequestParam(defaultValue = "0") Integer page) {
        PageResult<Issue> result = issueService.issuePage(user, repository, page);
        if (result == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }

    /*
     * 获取issue by number
     * */
    @GetMapping
    public ResponseEntity<Issue> issue(String user, String repository, Integer number) {
        Issue result = issueService.issue(user, repository, number);
        if (result == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }

    /*
     * 根据issue获取comment列表
     * */
    @GetMapping("comments")
    public ResponseEntity<List<IssueComment>> issueComments(Long issue) {
        List<IssueComment> result = issueService.issueComments(issue);
        if (result == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }

    @GetMapping("commentsByNumber")
    public ResponseEntity<List<IssueComment>> issueComments2(String user, String repository, Integer number) {
        List<IssueComment> result = issueService.issueComments(user, repository, number);
        if (result == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }


    /*
     * 创建issue
     * */
    @PostMapping
    public ResponseEntity<Boolean> issueCreate(Issue issue) {
        Boolean result = issueService.issueCreate(issue);
        if (result == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }

    /*
     * 创建issue comment
     * */
    @PostMapping("comment")
    public ResponseEntity<ResponseResult> issueCommentCreate(IssueComment comment) {
        ResponseResult result = issueService.issueCommentCreate(comment);
        if (result == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }

}// end
