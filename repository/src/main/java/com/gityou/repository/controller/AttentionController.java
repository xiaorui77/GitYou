package com.gityou.repository.controller;


import com.gityou.common.pojo.Attention;
import com.gityou.repository.service.AttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("attention")
public class AttentionController {
    @Autowired
    private AttentionService attentionService;


    /**
     * 获取attention信息
     */
    @GetMapping
    public ResponseEntity<Attention> query(Integer user, Long repository) {
        Attention result = attentionService.query(user, repository);
        if (result != null)
            return ResponseEntity.ok(result);
        else
            return ResponseEntity.notFound().build();
    }

    /**
     * 被watch的人
     */
    @GetMapping("watcher")
    public ResponseEntity<List> queryWatch(Long repository) {
        List result = attentionService.queryWatch(repository);
        if (result != null)
            return ResponseEntity.ok(result);
        else
            return ResponseEntity.notFound().build();
    }

    /**
     * 被star的人
     */
    @GetMapping("stargazer")
    public ResponseEntity<List> queryStar(Long repository) {
        List result = attentionService.queryStar(repository);
        if (result != null)
            return ResponseEntity.ok(result);
        else
            return ResponseEntity.notFound().build();
    }

    /**
     * 被fork的人
     */
    @GetMapping("forker")
    public ResponseEntity<List> queryFork(Long repository) {
        List result = attentionService.queryFork(repository);
        if (result != null)
            return ResponseEntity.ok(result);
        else
            return ResponseEntity.notFound().build();
    }


    /**
     * watch功能
     */
    @PutMapping("watch")
    public ResponseEntity<Boolean> watch(Attention watch) {
        Boolean result = attentionService.watch(watch);
        if (result != null)
            return ResponseEntity.ok(result);
        else
            return ResponseEntity.notFound().build();
    }

    /**
     * 星标
     */
    @PutMapping("star")
    public ResponseEntity<Boolean> star(Attention star) {
        Boolean result = attentionService.star(star);
        if (result != null)
            return ResponseEntity.ok(result);
        else
            return ResponseEntity.notFound().build();
    }

    /**
     * fork功能
     */
    @PutMapping("fork")
    public ResponseEntity<Boolean> fork(Attention fork) {
        Boolean result = attentionService.fork(fork);
        if (result != null)
            return ResponseEntity.ok(result);
        else
            return ResponseEntity.notFound().build();
    }


}
