package com.gityou.repository.utils;

import com.gityou.repository.entity.*;
import com.gityou.repository.gitblit.model.PathModel;
import com.gityou.repository.gitblit.model.RefModel;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
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

    public List<BranchResult> branchList(String user, String name) {
        StringBuilder temp = new StringBuilder(60).append(basePath).append(user).append("\\").append(name).append(".git\\.git");
        File localPath = new File(temp.toString());

        try {
            FileRepository repository = new FileRepository(localPath);
            List<RefModel> localBranches = JGitUtils.getLocalBranches(repository, false, Integer.MAX_VALUE);
            List<BranchResult> result = new ArrayList<>(localBranches.size());

            localBranches.forEach(e -> {
                BranchResult branch = new BranchResult();
                branch.setName(e.displayName);
                branch.setAuthor(e.getAuthorIdent().getEmailAddress());
                branch.setTime(e.getDate());
                result.add(branch);
            });
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 列出所有提交

    // 列出文件
    public List<FileResult> fileList(String user, String name, String branch, String path) {
        StringBuilder temp = new StringBuilder(60).append(basePath).append(user).append("\\").append(name).append(".git\\.git");
        File localPath = new File(temp.toString());

        try {
            FileRepository repository = new FileRepository(localPath);
            Git git = new Git(repository);

            // 获取branch的提交信息
            ObjectId branchId = JGitUtils.getBranch(repository, branch).getObjectId();
            RevCommit revCommit = repository.parseCommit(branchId);

            // 获取文件列表
            List<PathModel> filesInPath = JGitUtils.getFilesInPath(repository, path, revCommit);

            // 结果
            List<FileResult> result = new ArrayList<>(filesInPath.size());

            for (PathModel pathModel : filesInPath) {
                FileResult file = new FileResult();
                file.setName(pathModel.name);
                file.setFolder(!pathModel.isFile());

                Iterable<RevCommit> commit = git.log().addPath(pathModel.path).setMaxCount(1).call();
                commit.forEach(e -> {
                    file.setCommit(e.getName());
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

    // 最近一次提交
    public CommitResult lastCommit(String user, String name) {
        StringBuilder temp = new StringBuilder(60).append(basePath).append(user).append("\\").append(name).append(".git\\.git");
        File localPath = new File(temp.toString());


        try (Git git = Git.open(localPath)) {
            Iterable<RevCommit> revCommits = git.log().setMaxCount(1).call();
            CommitResult result = new CommitResult();
            revCommits.forEach(e -> {
                result.setEmail(e.getCommitterIdent().getEmailAddress());
                result.setName(e.getName());
                result.setMessage(e.getShortMessage());
                result.setTime(e.getCommitTime());
            });
            return result;
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
        return null;
    }

    // commit列表
    public List<CommitResult> commitList(String user, String name, String branch, Integer page) {
        StringBuilder temp = new StringBuilder(60).append(basePath).append(user).append("\\").append(name).append(".git\\.git");
        File localPath = new File(temp.toString());

        try (Git git = Git.open(localPath)) {
            List<CommitResult> result = new ArrayList<>();
            List<RevCommit> revCommits = JGitUtils.getRevLog(git.getRepository(), branch, (page - 1) * 28, 28);
            //Iterable<RevCommit> revCommits = git.log().set.setSkip().setMaxCount(28).call(); // 每页28

            revCommits.forEach(e -> {
                CommitResult c = new CommitResult();
                c.setEmail(e.getAuthorIdent().getEmailAddress());
                c.setName(e.getName());
                c.setMessage(e.getShortMessage());
                c.setTime(e.getCommitTime());
                result.add(c);
            });
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 文件修改列表
    public List<ChangeResult> changeList(String user, String name, String commit) {
        StringBuilder temp = new StringBuilder(60).append(basePath).append(user).append("\\").append(name).append(".git\\.git");
        File localPath = new File(temp.toString());

        try (org.eclipse.jgit.lib.Repository repository = new FileRepository(temp.toString())) {
            AnyObjectId commitId = ObjectId.fromString(commit);
            RevCommit revCommit = repository.parseCommit(commitId);

            List<PathModel.PathChangeModel> changeFiles = JGitUtils.getFilesInCommit(repository, revCommit);

            List<ChangeResult> result = new ArrayList<>(changeFiles.size());
            changeFiles.forEach(e -> {
                ChangeResult file = new ChangeResult();
                file.setName(e.name);
                file.setPath(e.path);
                file.setType(e.changeType.name());
                file.setInsertions(e.insertions);
                file.setDeletions(e.deletions);
                result.add(file);
            });
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 返回文件内容
    public FileContentResult fileContent(String user, String name, String branch, String path) {
        StringBuilder temp = new StringBuilder(60).append(basePath).append(user).append("\\").append(name).append(".git\\.git");
        File localPath = new File(temp.toString());

        try (org.eclipse.jgit.lib.Repository repository = new FileRepository(temp.toString())) {
            ObjectId branchId = JGitUtils.getBranch(repository, branch).getObjectId();
            RevCommit commit = repository.parseCommit(branchId);

            RevTree tree = commit.getTree();

            // 找打指定的文件
            TreeWalk treeWalk = new TreeWalk(repository);
            treeWalk.addTree(tree);
            treeWalk.setRecursive(true);
            treeWalk.setFilter(PathFilter.create(path));
            if (!treeWalk.next()) {
                System.out.println("错误");
            }

            ObjectId objectId = treeWalk.getObjectId(0);
            ObjectLoader loader = repository.open(objectId);

            // loader.copyTo(System.out);
            FileContentResult result = new FileContentResult();
            int index = path.lastIndexOf('/');
            if (index == -1)
                result.setName(path);
            else
                result.setName(path.substring(index + 1));
            result.setContent(loader.getBytes());

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}// end
