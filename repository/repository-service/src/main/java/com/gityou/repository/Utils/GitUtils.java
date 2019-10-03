package com.gityou.repository.Utils;

import com.gityou.repository.entity.FileResult;
import com.gityou.repository.gitblit.model.PathModel;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
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
    private static final String basePath = "D:\\tmp\\gityou\\repository\\";

    // 新建仓库
    public boolean createNewRepository(String user, String name) {
        StringBuilder temp = new StringBuilder(60).append(basePath).append(user).append("\\").append(name).append(".git");
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

    // 通过Url克隆仓库
    public boolean cloneRepository(String user, String name, String url) {
        StringBuilder temp = new StringBuilder(60).append(basePath).append(user).append("\\").append(name).append(".git");
        File repoPath = new File(temp.toString());

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


    // 列出所有分支(非远程)
    public List<String> getBranch(String user, String name) {
        StringBuilder temp = new StringBuilder(60).append(basePath).append(user).append("\\").append(name).append(".git");
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

    // 列出文件
    public List<FileResult> fileList(String user, String name) {
        StringBuilder temp = new StringBuilder(60).append(basePath).append(user).append("\\").append(name).append(".git\\.git");
        File localPath = new File(temp.toString());

        try {
            FileRepository repository = new FileRepository(localPath);
            Git git = new Git(repository);
            List<PathModel> filesInPath = JGitUtils.getFilesInPath(repository, null, null);

            List<FileResult> result = new ArrayList<>(filesInPath.size());

            for (PathModel pathModel : filesInPath) {
                FileResult file = new FileResult();
                file.setName(pathModel.name);
                file.setFolder(pathModel.isParentPath);

                Iterable<RevCommit> commit = git.log().addPath(pathModel.path).setMaxCount(1).call();
                commit.forEach(e -> {
                    file.setMessage(e.getShortMessage());
                    file.setTime(e.getCommitTime());
                });

                result.add(file);
            }
            return result;
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
        return null;
    }


}// end