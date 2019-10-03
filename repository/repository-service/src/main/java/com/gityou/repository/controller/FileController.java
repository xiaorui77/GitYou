package com.gityou.repository.controller;

import com.gityou.repository.entity.FileResult;
import com.gityou.repository.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("file")
public class FileController {

    @Autowired
    private FileService fileService;


    @GetMapping
    public ResponseEntity<List<FileResult>> fileList(String user, String name, @RequestParam(required = false) String path) {
        List<FileResult> fileResults = fileService.fileList(user, name, path);
        if (fileResults == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(fileResults);
    }

}// end
