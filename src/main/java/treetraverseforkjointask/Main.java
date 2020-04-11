package treetraverseforkjointask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ForkJoinPool;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final ForkJoinPool FORK_JOIN_POOL = new ForkJoinPool(2);

    public static void main(String[] args) {
        TreeNode firstLeaf = new TreeNode(Collections.emptyList(),false);
        TreeNode secondLeaf = new TreeNode(Collections.emptyList(),false);
        TreeNode firstChild = new TreeNode(Collections.singletonList(firstLeaf),false);
        TreeNode secondChild = new TreeNode(Collections.singletonList(secondLeaf),false);
        TreeNode root = new TreeNode(Arrays.asList(firstChild,secondChild),false);
        LOGGER.info("start");
        FORK_JOIN_POOL.invoke(new TreeTraverseTask(root));
        LOGGER.info("root isVisited {}; child isVisited {}; leaf idVisited {}",root.isVisited(),
                secondChild.isVisited(),firstLeaf.isVisited());

    }
}
