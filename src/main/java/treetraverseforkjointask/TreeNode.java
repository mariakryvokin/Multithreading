package treetraverseforkjointask;

import java.util.List;

public class TreeNode {
    private List<TreeNode> children;
    private boolean isVisited;

    public TreeNode(List<TreeNode> children, boolean isVisited) {
        this.children = children;
        this.isVisited = isVisited;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isVisited = visited;
    }

    public List<TreeNode> getChildren() {
        return children;
    }
}
