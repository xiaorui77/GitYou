package com.gityou.repository;

import com.gityou.repository.utils.GitUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.junit.Test;

import java.io.*;
import java.util.Iterator;
import java.util.List;

public class ApplicationTests {

    @Test
    public void contextLoads() throws Exception {
        Repository repository = new FileRepository("D:\\tmp\\gityou\\repository\\xiaorui\\test.git\\.git");

        /*
         * 变量
         * */
        String commit = "bffe6e91050fe8b27a28d67b9a3a64ac41a414a4";
        String path = "test";

        // ObjectId master = JGitUtils.getBranch(repository, "master").getReferencedObjectId();
        // RevCommit revCommit = repository.parseCommit(master);

        // AnyObjectId commitId = ObjectId.fromString("4ca8aa39b9822912b34616619337f5d0b82627d5");
        // RevCommit revCommit = repository.parseCommit(commitId);

        //AbstractTreeIterator oldTree = prepareTreeParser(repository, "9ddcc95f9e7c21a15b44f03199778f490f6cd0d6");
        //AbstractTreeIterator newTree = prepareTreeParser(repository, "a46e853114e324bf35d4c8101506b03d2804b86b");

        Git git = new Git(repository);

        // 获取两次提交
        ObjectId startObj = ObjectId.fromString(commit);
        Iterable<RevCommit> call = git.log().add(startObj).setSkip(1).call();

        Iterator<RevCommit> iterator = call.iterator();

        String oldId = null;
        if (iterator.hasNext()) {
            oldId = iterator.next().getId().getName();
        }
        //AbstractTreeIterator oldTree = prepareTreeParser(repository, oldId);
        AbstractTreeIterator newTree = prepareTreeParser(repository, commit);

        List<DiffEntry> diff = git.diff().setOldTree(null).setNewTree(newTree).call();

        for (DiffEntry entry : diff) {
            System.out.println("Entry: " + entry + ", from: " + entry.getOldId() + ", to: " + entry.getNewId());

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try (DiffFormatter formatter = new DiffFormatter(output)) {
                formatter.setRepository(repository);
                formatter.format(entry);
            }
            InputStream inputStream = new ByteArrayInputStream(output.toByteArray());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String diffHead = reader.readLine();
            String diffIndex = reader.readLine();
            String oldFile = reader.readLine();
            String newFile = reader.readLine();
            String change = reader.readLine();
            while (reader.ready()) {
                System.out.println(reader.readLine());
            }

            System.out.println();
        }

        System.out.println();
    }

    private static AbstractTreeIterator prepareTreeParser(Repository repository, String objectId) throws IOException {
        // from the commit we can build the tree which allows us to construct the TreeParser
        // noinspection Duplicates
        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(ObjectId.fromString(objectId));
            RevTree tree = walk.parseTree(commit.getTree().getId());

            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try (ObjectReader reader = repository.newObjectReader()) {
                treeParser.reset(reader, tree.getId());
            }
            walk.dispose();
            return treeParser;
        }
    }

    @Test
    public void test() throws IOException {
        GitUtils gitUtils = new GitUtils();
        Repository repository = new FileRepository("D:\\tmp\\gityou\\repository\\xiaorui\\test5.git\\.git");
        //List<ChangeResult> changeResults = gitUtils.changeList("xiaorui", "test", "928d184c1c83cd8954b08754a7b4153028bfecee");
        // DiffResult diff = gitUtils.diff("xiaorui", "test", "928d184c1c83cd8954b08754a7b4153028bfecee", "test");


        System.out.println();
    }

}
