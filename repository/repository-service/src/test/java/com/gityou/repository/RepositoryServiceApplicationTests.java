package com.gityou.repository;

import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoryServiceApplicationTests {

    //@Test
    public void contextLoads() throws Exception {
        Repository repository = new FileRepository("D:\\tmp\\gityou\\repository\\xiaorui\\test5.git\\.git");

        // ObjectId master = JGitUtils.getBranch(repository, "master").getReferencedObjectId();
        // RevCommit revCommit = repository.parseCommit(master);

        // AnyObjectId commitId = ObjectId.fromString("4ca8aa39b9822912b34616619337f5d0b82627d5");
        // RevCommit revCommit = repository.parseCommit(commitId);

        /*
         * 获取文件内容
         * */
        ObjectId lastCommitId = repository.resolve(Constants.HEAD);

        try (RevWalk revWalk = new RevWalk(repository)) {
            RevCommit commit = revWalk.parseCommit(lastCommitId);
            RevTree tree = commit.getTree();
            System.out.println("得到的树: " + tree);

            // 找打指定的文件
            TreeWalk treeWalk = new TreeWalk(repository);
            treeWalk.addTree(tree);
            treeWalk.setRecursive(true);
            treeWalk.setFilter(PathFilter.create("readme.md"));
            if (!treeWalk.next()) {
                System.out.println("错误");
            }

            ObjectId objectId = treeWalk.getObjectId(0);
            ObjectLoader loader = repository.open(objectId);

            loader.copyTo(System.out);
            String str = new String(loader.getBytes());
            System.out.println();
        }


        // JGitUtils.getCommit();
        // List<PathModel.PathChangeModel> filesInCommit = JGitUtils.getFilesInCommit(repository, revCommit);

        System.out.println();
    }

}
