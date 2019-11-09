package com.gityou.repository.controller;


import com.gityou.repository.pojo.Star;
import com.gityou.repository.service.StarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("attention")
public class StarController {
    @Autowired
    private StarService starService;


    /*
     * 获取attention信息
     * */
    @GetMapping
    public ResponseEntity<Star> query(Integer user, Long repository) {
        Star result = starService.query(user, repository);
        if (result != null)
            return ResponseEntity.ok(result);
        else
            return ResponseEntity.notFound().build();
    }


    /*
     * watch
     * @argument user: 订阅人(已登录的用户)
     * @argument repository: 要订阅的repository
     * */
    @PutMapping("watch")
    public ResponseEntity<Boolean> watch(Star star) {
        Boolean result = starService.watch(star);
        if (result != null)
            return ResponseEntity.ok(result);
        else
            return ResponseEntity.notFound().build();
    }

    /*
     *星标
     * */
    @PutMapping("star")
    public ResponseEntity<Boolean> star(Star star) {
        Boolean result = starService.star(star);
        if (result != null)
            return ResponseEntity.ok(result);
        else
            return ResponseEntity.notFound().build();
    }

    /*
     * fork功能
     * */
    public ResponseEntity<Boolean> fork(Star star) {
        Boolean result = starService.fork(star);
        if (result != null)
            return ResponseEntity.ok(result);
        else
            return ResponseEntity.notFound().build();
    }


}
