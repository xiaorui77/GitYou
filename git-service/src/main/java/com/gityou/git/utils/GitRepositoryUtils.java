package com.gityou.git.utils;


import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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


    @Value("${gityou.repositoryPath}")
    public void setBasePath(String repositoryPath) {
        basePath = repositoryPath + Separator;
    }

    @Value("${gityou.repositorySuffix}")
    public void setRepositorySuffix(String repositorySuffix) {
        GitRepositoryUtils.repositorySuffix = repositorySuffix;
    }


    /**
     * 新建仓库
     */
    public boolean createNewRepository(String user, String name) {
        File repoPath = new File(basePath + user + Separator + name + ".git");

        // 创建仓库
        try (Git git = Git.init().setBare(false).setDirectory(repoPath).call()) {
            LOGGER.info("成功创建仓库: " + git.getRepository().getDirectory());
            return true;
        } catch (GitAPIException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    /**
     * 通过Url克隆仓库
     */
    public boolean importRepository(String user, String name, String url) {
        if (StringUtils.isEmpty(url))
            return false;

        File repoPath = new File(basePath + user + Separator + name + ".git");

        try (Git git = Git.cloneRepository().setURI(url)
                .setCloneAllBranches(true)  // 克隆所有分支
                .setDirectory(repoPath)
                .setTimeout(5).call()) {
            LOGGER.info("成功创建仓库: " + git.getRepository().getDirectory());
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

} // end

