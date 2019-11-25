package com.gityou.repository.controller;

import com.gityou.common.entity.PageResult;
import com.gityou.common.entity.ResponseResult;
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


    /**
     * 根据user查询 Repository
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Repository>> queryRepos(
            @RequestParam String user,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "0") Integer type,
            @RequestParam(defaultValue = "0") Integer language,
            @RequestParam(required = false) String keyword) {
        page = page < 1 ? 1 : page;
        PageResult<Repository> repositories = repoService.queryRepos(user, page - 1, type, language, keyword);
        if (repositories == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(repositories);
    }

    /**
     * 根据username查询 用户的popular仓库
     * 默认查询8条
     */
    @GetMapping("popular")
    public ResponseEntity<List<Repository>> queryPopularRepos(@RequestParam String user, @RequestParam(defaultValue = "8") Integer num) {
        List<Repository> repositories = repoService.queryPopularRepos(user, num);
        if (repositories == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(repositories);
    }

    /**
     * 根据仓库id 查询单个Repository信息
     */
    @GetMapping
    public ResponseEntity<Repository> queryRepository(Long repository) {
        Repository result = repoService.queryRepository(repository);
        if (result == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }

    /**
     * 根据用户名,仓库名 查询单个Repository信息
     */
    @GetMapping("name")
    public ResponseEntity<Repository> queryRepositoryByName(String user, String name) {
        Repository result = repoService.queryRepositoryByName(user, name);
        if (result == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }

    /**
     * 创建 Git 仓库
     */
    @PostMapping("new")
    public ResponseEntity<ResponseResult> createRepository(Repository repository) {
        ResponseResult result = repoService.createRepository(repository);
        if (result.getCode() == 200)
            return ResponseEntity.ok(result);
        else
            return ResponseEntity.status(result.getCode()).body(result);
    }

    /**
     * 导入 仓库
     */
    @PostMapping("import")
    public ResponseEntity<ResponseResult> importRepository(Repository repository) {
        ResponseResult result = repoService.importRepository(repository);
        if (result.getCode() == 200)
            return ResponseEntity.ok(result);
        else
            return ResponseEntity.status(result.getCode()).body(result);
    }

}// end
