//TreeNode to store book information in RedBlackTree
public class GatorTreeNode {
    public GatorBook book;
    public int color;
    public GatorTreeNode left;
    public GatorTreeNode right;
    public GatorTreeNode parent;
    // 1 -> RED, 0 -> BLACK
    GatorTreeNode(GatorBook book) {
        this.book = book;
        this.color = 1;
        this.left = null;
        this.right = null;
        this.parent = null;
    }
}
