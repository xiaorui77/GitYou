package com.gityou.repository.controller;

import com.gityou.common.entity.PageResult;
import com.gityou.repository.pojo.Repository;
import com.gityou.repository.service.RepositoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
 * Repository Controller
 * */
@RestController
@RequestMapping("repository")
public class RepositoryController {
    private final RepositoryService repoService;

    public RepositoryController(RepositoryService repoService) {
        this.repoService = repoService;
    }


    /*
     * 根据user查询 Repository
     * */
    @GetMapping("page")
    public ResponseEntity<PageResult<Repository>> queryRepos(
            @RequestParam Integer userId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer type,
            @RequestParam(defaultValue = "0") Integer language,
            @RequestParam(required = false) String keyword) {
        PageResult<Repository> repositories = repoService.queryRepos(userId, page, type, language, keyword);
        if (repositories == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(repositories);
    }

    /*
     * 根据user查询 popular Repository
     * */
    @GetMapping("popular")
    public ResponseEntity<List<Repository>> queryPopularRepos(@RequestParam Integer userId) {
        List<Repository> repositories = repoService.queryPopularRepos(userId);
        if (repositories == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(repositories);
    }

    /*
     * 根据id查询单个 Repository
     * */
    @GetMapping
    public ResponseEntity<Repository> queryRepository(@RequestParam Long id) {
        // Todo
        return ResponseEntity.notFound().build();
    }

}
