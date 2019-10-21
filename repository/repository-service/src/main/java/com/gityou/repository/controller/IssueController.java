package com.gityou.repository.controller;

import com.gityou.common.entity.PageResult;
import com.gityou.repository.pojo.Issue;
import com.gityou.repository.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("issue")
public class IssueController {
    @Autowired
    private IssueService issueService;

    /*
     * 根据仓库id获取issue列表
     * */
    @RequestMapping("page")
    public ResponseEntity<PageResult<Issue>> issuePage(String repository, @RequestParam(defaultValue = "0") Integer page) {
        PageResult<Issue> result = issueService.issuePage(repository, page);
        if (result == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }

}// end
