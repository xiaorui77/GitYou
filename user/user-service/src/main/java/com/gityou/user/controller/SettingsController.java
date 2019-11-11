package com.gityou.user.controller;


import com.gityou.common.pojo.Email;
import com.gityou.common.pojo.Settings;
import com.gityou.user.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("settings")
public class SettingsController {
    @Autowired
    private SettingsService settingsService;

    @GetMapping
    public ResponseEntity<Settings> query(Integer user) {
        Settings result = settingsService.query(user);
        if (result == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }

    @GetMapping("email")
    public ResponseEntity<List<Email>> queryEmails(Integer user) {
        List<Email> result = settingsService.queryEmails(user);
        if (result == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }

    @PutMapping("notification")
    public ResponseEntity<Boolean> modifyNotification(Integer user, String key, Integer value) {
        Boolean result = settingsService.modifyNotification(user, key, value);
        if (result == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }

    @PutMapping("email")
    public ResponseEntity<Boolean> modifyEmail(Integer user, String key, String value) {
        Boolean result = settingsService.modifyEmail(user, key, value);
        if (result == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(result);
    }

}// end
