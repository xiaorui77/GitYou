package com.gityou.user.controller;


import com.gityou.common.entity.PageResult;
import com.gityou.user.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("message")
public class MessageController {
    @Autowired
    private MessageService messageService;


    @GetMapping("page")
    public ResponseEntity<PageResult> messagePage(
            Integer user, @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "0") Integer status,
            @RequestParam(defaultValue = "0") Integer type) {
        PageResult result = messageService.messagePage(user, page, status, type);
        if (result == null)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.ok(result);
    }

}// end
