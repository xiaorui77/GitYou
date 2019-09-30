package com.gityou.repository.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GitUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(GitUtils.class);
    private static StringBuilder basePath = new StringBuilder("D:\\tmp\\gityou\\repository\\");

    // 新建仓库
    public void createNewRepository(String user, String name) {
        StringBuilder temp = basePath.append(user).append("\\").append(name);
        File repoPath = new File(temp.toString());

        // 创建仓库
        try (Git git = Git.init().setBare(false).setDirectory(repoPath).call()) {
            LOGGER.info("成功创建仓库: " + git.getRepository().getDirectory());
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    // 通过Url克隆仓库
    public void cloneRepository(String user, String name, String url) {
        StringBuilder temp = basePath.append(user).append("\\").append(name);
        File repoPath = new File(temp.toString());

        try (Git git = Git.cloneRepository().setURI(url)
                .setCloneAllBranches(true)  // 克隆所有分支
                .setDirectory(repoPath).call()) {
            LOGGER.info("成功创建仓库: " + git.getRepository().getDirectory());
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    // 列出所有分支(非远程)
    public List<String> getBranch(String user, String name) {
        StringBuilder temp = basePath.append(user).append("\\").append(name).append(".git");
        File localPath = new File(temp.toString());

        try (Git git = Git.open(localPath)) {
            List<Ref> call = git.branchList().call();
            List<String> result = new ArrayList<>(call.size());
            call.forEach(e -> {
                String str = e.getName();
                result.add(str.substring(str.lastIndexOf("/") + 1));
            });
            return result;
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 列出所有提交

}
