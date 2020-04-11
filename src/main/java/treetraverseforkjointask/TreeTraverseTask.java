package treetraverseforkjointask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

public class TreeTraverseTask extends RecursiveAction {
    private TreeNode treeToTraverse;
    private static final Logger LOGGER = LogManager.getLogger();

    public TreeTraverseTask(TreeNode treeToTraverse) {
        this.treeToTraverse = treeToTraverse;
    }


    @Override
    protected void compute() {
        LOGGER.info("compute");
        treeToTraverse.setVisited(true);
        List<TreeTraverseTask> subTasks = new LinkedList<>();
        for (TreeNode node : treeToTraverse.getChildren()) {
            TreeTraverseTask treeTraverseTask = new TreeTraverseTask(node);
            treeTraverseTask.fork();
            subTasks.add(treeTraverseTask);
        }
        for (TreeTraverseTask task : subTasks) {
            task.join();
        }
    }
}
