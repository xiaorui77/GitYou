package com.gityou.repository.utils;

import com.gityou.common.entity.PageResult;
import com.gityou.repository.entity.*;
import com.gityou.repository.gitblit.model.PathModel;
import com.gityou.repository.gitblit.model.RefModel;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GitUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(GitUtils.class);
    private static final String basePath = "D:\\tmp\\gityou\\repository\\";

    private static final int PageSize = 28;

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

                Iterable<RevCommit> commit = git.log().add(revCommit.getId()).addPath(pathModel.path).setMaxCount(1).call();
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

    // 获取提交
    public CommitResult query(String user, String name, String commit) {
        File localPath = new File(basePath + user + "\\" + name + ".git\\.git");

        try (org.eclipse.jgit.lib.Repository repository = new FileRepository(localPath)) {
            RevCommit revCommit = repository.parseCommit(ObjectId.fromString(commit));
            CommitResult result = new CommitResult();
            result.setName(commit);
            result.setMessage(revCommit.getShortMessage());
            result.setFullMessage(revCommit.getFullMessage());
            result.setEmail(revCommit.getAuthorIdent().getEmailAddress());
            result.setTime(revCommit.getCommitTime());
            return result;
        } catch (IOException e) {
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
    public PageResult<CommitResult> commitList(String user, String name, String branch, String author, Integer page) {
        File localFile = new File(basePath + user + "\\" + name + ".git\\.git");

        try (org.eclipse.jgit.lib.Repository repository = new FileRepository(localFile)) {
            List<CommitResult> commits = new ArrayList<>();
            // List<RevCommit> revCommits = JGitUtils.getRevLog(repository, branch, (page - 1) * 28, 28);
            
            /* new begin*/
            ObjectId branchObject;
            if (StringUtils.isEmpty(branch))
                branchObject = repository.resolve("master");
            else
                branchObject = repository.resolve(branch);

            RevWalk revWalk = new RevWalk(repository);
            RevCommit head = revWalk.parseCommit(branchObject);
            revWalk.markStart(head);

            int start = (page - 1) * PageSize;
            int count = 0;
            for (RevCommit rev : revWalk) {
                count++;
                if (count <= start)
                    continue;
                if (author != null && !rev.getAuthorIdent().getEmailAddress().equals(author))
                    continue;

                if (commits.size() < PageSize) {
                    CommitResult c = new CommitResult();
                    c.setEmail(rev.getAuthorIdent().getEmailAddress());
                    c.setName(rev.getName());
                    c.setMessage(rev.getShortMessage());
                    c.setTime(rev.getCommitTime());
                    commits.add(c);
                }
            }
            int pageNum = (int) Math.ceil((double) count / PageSize);
            PageResult<CommitResult> result = new PageResult<>(count, pageNum, commits);
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

    // 文件修改列表
    public List<FileDiffResult> changeList(String user, String name, String commit) {
        StringBuilder temp = new StringBuilder(60).append(basePath).append(user).append("\\").append(name).append(".git\\.git");
        File localPath = new File(temp.toString());

        try (org.eclipse.jgit.lib.Repository repository = new FileRepository(temp.toString())) {
            RevCommit revCommit = repository.parseCommit(ObjectId.fromString(commit));
            RevWalk revWalk = new RevWalk(repository);

            // 如果是根提交
            if (revCommit.getParentCount() == 0) {
                List<FileDiffResult> result = new ArrayList<>();
                TreeWalk tw = new TreeWalk(repository);
                tw.reset();
                tw.setRecursive(true);
                tw.addTree(revCommit.getTree());
                while (tw.next()) {
                    ObjectId objectId = tw.getObjectId(0);
                    byte[] bytes = tw.getObjectReader().open(objectId).getBytes();

                    FileDiffResult file = new FileDiffResult();
                    file.setName(tw.getPathString());
                    file.setPath(tw.getPathString());
                    file.setType(DiffEntry.ChangeType.ADD.toString());
                    file.setStatistics("@@ -0,0 +0,0 @@");
                    file.setDiff(new String(bytes));
                    result.add(file);
                }
                tw.close();
                return result;
            }

            // 获取上个提交
            RevCommit parentCommit = revWalk.parseCommit(revCommit.getParent(0).getId());
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            DiffFormatter df = new DiffFormatter(output);
            df.setRepository(repository);
            df.setDiffComparator(RawTextComparator.DEFAULT);
            df.setDetectRenames(true); // 检测重用名

            List<DiffEntry> diffs = df.scan(parentCommit.getTree(), revCommit.getTree());
            List<FileDiffResult> result = new ArrayList<>(diffs.size());    // 返回的结果
            for (DiffEntry diff : diffs) {
                FileDiffResult file = new FileDiffResult();
                file.setType(diff.getChangeType().toString());  // 设置Type
                file.setName(diff.getNewPath().substring(diff.getNewPath().lastIndexOf('/') + 1));  // 设置name
                file.setPath(diff.getNewPath());
                if (diff.getChangeType().equals(DiffEntry.ChangeType.DELETE) || diff.getChangeType().equals(DiffEntry.ChangeType.RENAME))
                    file.setOldPath(diff.getOldPath());

                df.format(diff);
                InputStream inputStream = new ByteArrayInputStream(output.toByteArray());
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                // 分离结果
                while (reader.ready()) {
                    String str = reader.readLine();
                    if (str.startsWith("@@") && str.endsWith("@@")) {
                        file.setStatistics(str);
                        StringBuilder stringBuffer = new StringBuilder();
                        String line = "";
                        while ((line = reader.readLine()) != null)
                            stringBuffer.append(line).append("\n"); // 可以进一步性能优化
                        file.setDiff(stringBuffer.toString());
                        break;
                    }
                }
                output.reset();
                result.add(file);
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 文件差异 修改类型为MODIFY的才可以调用
    public DiffResult diff(String user, String name, String commit, String path) {
        StringBuilder temp = new StringBuilder(60).append(basePath).append(user).append("\\").append(name).append(".git\\.git");
        File localPath = new File(temp.toString());

        try (org.eclipse.jgit.lib.Repository repository = new FileRepository(temp.toString())) {
            Git git = new Git(repository);

            //Iterator<RevCommit> iterator = git.log().add(ObjectId.fromString(commit)).setSkip(1).call().iterator();

            RevCommit revCommit = repository.parseCommit(ObjectId.fromString(commit));

            // 如果没有证明是第一条
            if (revCommit.getParentCount() == 0) {
                return null;
            }

            AbstractTreeIterator oldTree = prepareTreeParser(repository, revCommit.getParent(0));
            AbstractTreeIterator newTree = prepareTreeParser(repository, revCommit);

            List<DiffEntry> diff = git.diff().setPathFilter(PathFilter.create(path)).setOldTree(oldTree).setNewTree(newTree).call();

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            DiffResult result = new DiffResult();

            for (DiffEntry entry : diff) {
                result.setType(entry.getChangeType().toString());
                try (DiffFormatter formatter = new DiffFormatter(output)) {
                    formatter.setRepository(repository);
                    formatter.format(entry);
                    break;
                }
            }
            InputStream inputStream = new ByteArrayInputStream(output.toByteArray());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            // 分离结果
            while (reader.ready()) {
                String str = reader.readLine();
                if (str.startsWith("@@") && str.endsWith("@@")) {
                    result.setStatistics(str);
                    StringBuffer stringBuffer = new StringBuffer();
                    String line = "";
                    while ((line = reader.readLine()) != null)
                        stringBuffer.append(line).append("\n");
                    result.setDiff(stringBuffer.toString());
                    break;
                }
            }
            return result;
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
        return null;
    }


    /*  static util function*/
    private static AbstractTreeIterator prepareTreeParser(org.eclipse.jgit.lib.Repository repository, ObjectId objectId) throws IOException {
        // from the commit we can build the tree which allows us to construct the TreeParser
        // noinspection Duplicates
        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(objectId);
            RevTree tree = walk.parseTree(commit.getTree().getId());

            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try (ObjectReader reader = repository.newObjectReader()) {
                treeParser.reset(reader, tree.getId());
            }
            walk.dispose();
            return treeParser;
        }
    }


}// end
