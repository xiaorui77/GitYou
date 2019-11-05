package com.gityou.repository.controller;


import com.gityou.repository.pojo.Star;
import com.gityou.repository.service.StarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("star")
public class StarController {
    @Autowired
    private StarService starService;

    /*
     *星标
     * */
    @PutMapping("star")
    public ResponseEntity<Boolean> star(Star star) {
        Boolean result = starService.star(star);
        if (result != null)
            return ResponseEntity.ok(true);
        else
            return ResponseEntity.notFound().build();
    }


}
