package com.gityou.repository.controller;

import com.gityou.common.entity.PageResult;
import com.gityou.common.entity.RequestResult;
import com.gityou.common.pojo.Repository;
import com.gityou.repository.service.RepositoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam String user,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer type,
            @RequestParam(defaultValue = "0") Integer language,
            @RequestParam(required = false) String keyword) {
        PageResult<Repository> repositories = repoService.queryRepos(user, page, type, language, keyword);
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
     * 详细信息
     * */
    @GetMapping
    public ResponseEntity<Repository> queryRepository(@RequestParam Long id) {
        // Todo
        return ResponseEntity.notFound().build();
    }

    /* 根据 user, name查询单个 Repository
     * 自己的仓库
     * */
    @GetMapping("name")
    public ResponseEntity<Repository> queryRepositoryByName(String user, String name) {
        Repository result = repoService.queryRepositoryByName(user, name);
        if (result == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }

    /*
     * 创建 Git 仓库
     * */
    @PostMapping
    public ResponseEntity<RequestResult> createRepository(Repository repository) {
        RequestResult result = repoService.createRepository(repository);
        if (result.getCode() == 200)
            return ResponseEntity.ok(result);
        else
            return ResponseEntity.status(result.getCode()).body(result);
    }

    /* 导入 仓库
     * */
    @PostMapping("import")
    public ResponseEntity<RequestResult> importRepository(Repository repository, String clone) {
        RequestResult result = repoService.importRepository(repository, clone);
        if (result.getCode() == 200)
            return ResponseEntity.ok(result);
        else
            return ResponseEntity.status(result.getCode()).body(result);
    }

}// end
