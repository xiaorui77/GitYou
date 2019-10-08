package com.gityou.repository.service;


import com.gityou.repository.utils.GitUtils;
import com.gityou.repository.entity.FileResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    @Autowired
    private GitUtils gitUtils;


    // 返回文件列表
    public List<FileResult> fileList(String user, String name, String branch, String path) {
        return gitUtils.fileList(user, name, branch, path);
    }

}// end
