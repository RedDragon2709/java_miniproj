package bst;

public class BST {

    private BSTNode root;

    public BSTNode getRoot() {
        return root;
    }

    // ---------- INSERT ----------
    public boolean insert(int data) {
        if (search(data)) return false;   // prevent duplicates
        root = insertRec(root, data);
        return true;
    }

    private BSTNode insertRec(BSTNode node, int data) {
        if (node == null)
            return new BSTNode(data);

        if (data < node.getData())
            node.setLeft(insertRec(node.getLeft(), data));
        else
            node.setRight(insertRec(node.getRight(), data));

        return node;
    }

    // ---------- SEARCH ----------
    public boolean search(int data) {
        return searchRec(root, data);
    }

    private boolean searchRec(BSTNode node, int data) {
        if (node == null) return false;
        if (data == node.getData()) return true;

        return data < node.getData()
                ? searchRec(node.getLeft(), data)
                : searchRec(node.getRight(), data);
    }

    // ---------- DELETE ----------
    public void delete(int data) {
        root = deleteRec(root, data);
    }

    private BSTNode deleteRec(BSTNode node, int data) {
        if (node == null) return null;

        if (data < node.getData()) {
            node.setLeft(deleteRec(node.getLeft(), data));
        } else if (data > node.getData()) {
            node.setRight(deleteRec(node.getRight(), data));
        } else {
            // Case 1 & 2
            if (node.getLeft() == null) return node.getRight();
            if (node.getRight() == null) return node.getLeft();

            // Case 3
            node.setData(minValue(node.getRight()));
            node.setRight(deleteRec(node.getRight(), node.getData()));
        }
        return node;
    }

    private int minValue(BSTNode node) {
        while (node.getLeft() != null)
            node = node.getLeft();
        return node.getData();
    }

    // ---------- TRAVERSALS ----------
    public String inorder() {
        StringBuilder sb = new StringBuilder();
        inorderRec(root, sb);
        return sb.toString();
    }

    private void inorderRec(BSTNode node, StringBuilder sb) {
        if (node == null) return;
        inorderRec(node.getLeft(), sb);
        sb.append(node.getData()).append(" ");
        inorderRec(node.getRight(), sb);
    }

    public String preorder() {
        StringBuilder sb = new StringBuilder();
        preorderRec(root, sb);
        return sb.toString();
    }

    private void preorderRec(BSTNode node, StringBuilder sb) {
        if (node == null) return;
        sb.append(node.getData()).append(" ");
        preorderRec(node.getLeft(), sb);
        preorderRec(node.getRight(), sb);
    }

    public String postorder() {
        StringBuilder sb = new StringBuilder();
        postorderRec(root, sb);
        return sb.toString();
    }

    private void postorderRec(BSTNode node, StringBuilder sb) {
        if (node == null) return;
        postorderRec(node.getLeft(), sb);
        postorderRec(node.getRight(), sb);
        sb.append(node.getData()).append(" ");
    }

    // ---------- HEIGHT ----------
    public int height() {
        return heightRec(root);
    }

    private int heightRec(BSTNode node) {
        if (node == null) return 0;
        return 1 + Math.max(heightRec(node.getLeft()), heightRec(node.getRight()));
    }

    // ---------- CLEAR ----------
    public void clear() {
        root = null;
    }
}
