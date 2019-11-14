package com.gityou.repository.controller;

import com.gityou.common.entity.PageResult;
import com.gityou.common.pojo.Issue;
import com.gityou.common.pojo.IssueComment;
import com.gityou.repository.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("issue")
public class IssueController {
    @Autowired
    private IssueService issueService;

    /*
     * 根据仓库id获取issue列表
     * */
    @RequestMapping("page")
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
    @RequestMapping
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
    @RequestMapping("comments")
    public ResponseEntity<List<IssueComment>> issueComments(Long issue) {
        List<IssueComment> result = issueService.issueComments(issue);
        if (result == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }

    @RequestMapping("commentsByNumber")
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

}// end
