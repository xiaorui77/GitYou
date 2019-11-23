package com.gityou.git.utils;


import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.File;

@Repository
public class GitRepositoryUtils {

    // logger
    private static final Logger LOGGER = LoggerFactory.getLogger(GitRepositoryUtils.class);

    // 系统相关分隔符
    private static final String Separator = File.separator;

    // 仓库位置
    private static String basePath;
    // 后缀
    private static String repositorySuffix;

    // 文件名一般长度
    private static final int FilePathLength = 80;


    /**
     * 新建仓库
     */
    public boolean createNewRepository(String user, String name) {
        StringBuilder temp = new StringBuilder(FilePathLength).append(basePath).append(user).append(Separator).append(name).append(".git");
        File repoPath = new File(temp.toString());

        // 创建仓库
        try (Git git = Git.init().setBare(false).setDirectory(repoPath).call()) {
            LOGGER.info("成功创建仓库: " + git.getRepository().getDirectory());
            return true;
        } catch (GitAPIException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

} // end

