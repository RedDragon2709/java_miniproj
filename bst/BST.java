package bst;

public class BST {

    private BSTNode root;

    public BSTNode getRoot() {
        return root;
    }

    // Insert
    public void insert(int data) {
        root = insertRec(root, data);
    }

    private BSTNode insertRec(BSTNode root, int data) {
        if (root == null)
            return new BSTNode(data);

        if (data < root.data)
            root.left = insertRec(root.left, data);
        else if (data > root.data)
            root.right = insertRec(root.right, data);

        return root;
    }

    // Delete
    public void delete(int data) {
        root = deleteRec(root, data);
    }

    private BSTNode deleteRec(BSTNode root, int data) {
        if (root == null)
            return null;

        if (data < root.data)
            root.left = deleteRec(root.left, data);
        else if (data > root.data)
            root.right = deleteRec(root.right, data);
        else {
            if (root.left == null)
                return root.right;
            if (root.right == null)
                return root.left;

            root.data = minValue(root.right);
            root.right = deleteRec(root.right, root.data);
        }
        return root;
    }

    private int minValue(BSTNode root) {
        int min = root.data;
        while (root.left != null) {
            min = root.left.data;
            root = root.left;
        }
        return min;
    }

    // Traversals
    public String inorder() {
        return inorderRec(root);
    }

    private String inorderRec(BSTNode root) {
        if (root == null) return "";
        return inorderRec(root.left) + root.data + " " + inorderRec(root.right);
    }

    public String preorder() {
        return preorderRec(root);
    }

    private String preorderRec(BSTNode root) {
        if (root == null) return "";
        return root.data + " " + preorderRec(root.left) + preorderRec(root.right);
    }

    public String postorder() {
        return postorderRec(root);
    }

    private String postorderRec(BSTNode root) {
        if (root == null) return "";
        return postorderRec(root.left) + postorderRec(root.right) + root.data + " ";
    }

    // Height
    public int height() {
        return heightRec(root);
    }

    private int heightRec(BSTNode root) {
        if (root == null) return 0;
        return 1 + Math.max(heightRec(root.left), heightRec(root.right));
    }
}
