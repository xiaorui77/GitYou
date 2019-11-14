package com.gityou.repository.controller;


import com.gityou.common.entity.FileContentResult;
import com.gityou.common.entity.FileResult;
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

    /*
     * 获取文件列表
     * */
    @GetMapping("list")
    public ResponseEntity<List<FileResult>> fileList(
            String user, String name, @RequestParam(defaultValue = "master") String branch,
            @RequestParam(required = false) String path) {
        List<FileResult> fileResults = fileService.fileList(user, name, branch, path);
        if (fileResults == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(fileResults);
    }

    /*
     * 获取文件内容
     * */
    @GetMapping
    public ResponseEntity<FileContentResult> fileContent(
            String user, String name, @RequestParam(defaultValue = "master") String branch,
            @RequestParam(required = false) String path) {
        FileContentResult fileContent = fileService.fileContent(user, name, branch, path);
        if (fileContent == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(fileContent);
    }

    @GetMapping("change")
    public ResponseEntity changeList(String user, String name, @RequestParam(defaultValue = "master") String branch,
                                     @RequestParam(required = false) String path) {
        // TODO
        return ResponseEntity.notFound().build();
    }

}// end
