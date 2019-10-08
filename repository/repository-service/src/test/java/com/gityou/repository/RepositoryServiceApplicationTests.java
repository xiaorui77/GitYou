package com.gityou.repository;

import com.gityou.repository.utils.JGitUtils;
import com.gityou.repository.gitblit.model.PathModel;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoryServiceApplicationTests {

    // @Test
    public void contextLoads() throws Exception {
        Repository repository = new FileRepository("D:\\tmp\\gityou\\repository\\xiaorui\\test5.git\\.git");

        ObjectId master = JGitUtils.getBranch(repository, "master").getReferencedObjectId();
        RevCommit revCommit = repository.parseCommit(master);

        // JGitUtils.getCommit();
        RevCommit commit = JGitUtils.getCommit(repository, master.toString());
        List<PathModel> filesInPath = JGitUtils.getFilesInPath(repository, "新建文件夹", revCommit);


        System.out.println();
    }

}
