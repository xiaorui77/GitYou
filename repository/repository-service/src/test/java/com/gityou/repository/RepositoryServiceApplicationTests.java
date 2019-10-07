package com.gityou.repository;

import com.gityou.repository.Utils.JGitUtils;
import com.gityou.repository.gitblit.model.RefModel;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoryServiceApplicationTests {

    @Test
    public void contextLoads() throws Exception {
        Repository repository = new FileRepository("D:\\tmp\\gityou\\repository\\xiaorui\\test5.git\\.git");

        RefModel master = JGitUtils.getBranch(repository, "master");
        ObjectId defaultBranch = JGitUtils.getDefaultBranch(repository);
        List<RefModel> localBranches = JGitUtils.getLocalBranches(repository, false, Integer.MAX_VALUE);
        List<RefModel> noteBranches = JGitUtils.getNoteBranches(repository, true, Integer.MAX_VALUE);
        RefModel pagesBranch = JGitUtils.getPagesBranch(repository);
        List<RefModel> remoteBranches = JGitUtils.getRemoteBranches(repository, true, Integer.MAX_VALUE);
        System.out.println();
    }

}
