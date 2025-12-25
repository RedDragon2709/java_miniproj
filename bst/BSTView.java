package bst;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BSTView extends JFrame {

    private JTextField tf;
    private JButton btnAdd, btnDelete, btnSearch, btnClear;
    private JLabel lblHeight, lblStatus;
    private JTextArea taTraversals;
    private TreePanel treePanel;

    private BST bst;
    private Integer searchedValue = null;

    // Fonts
    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);

    public BSTView(BST bst) {
        this.bst = bst;
        initUI();
    }

    private void initUI() {
        setTitle("Binary Search Tree Visualizer");
        setSize(1100, 720);
        setLayout(new BorderLayout(10, 10));
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    // ================= TOP =================
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel title = new JLabel("Binary Search Tree Visualization");
        title.setFont(FONT_TITLE);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));

        tf = new JTextField(10);
        tf.setFont(FONT_NORMAL);

        btnAdd = createButton("Insert");
        btnDelete = createButton("Delete");
        btnSearch = createButton("Search");
        btnClear = createButton("Clear");

        controls.add(new JLabel("Value:"));
        controls.add(tf);
        controls.add(btnAdd);
        controls.add(btnDelete);
        controls.add(btnSearch);
        controls.add(btnClear);

        panel.add(title, BorderLayout.WEST);
        panel.add(controls, BorderLayout.EAST);

        tf.addActionListener(e -> insertAction());
        btnAdd.addActionListener(e -> insertAction());
        btnDelete.addActionListener(e -> deleteAction());
        btnSearch.addActionListener(e -> searchAction());
        btnClear.addActionListener(e -> clearAction());

        return panel;
    }

    // ================= CENTER =================
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(0, 15, 0, 15));

        treePanel = new TreePanel(bst);

        JPanel infoPanel = new JPanel(new BorderLayout(5, 5));
        infoPanel.setPreferredSize(new Dimension(320, 500));

        JLabel infoTitle = new JLabel("Tree Information");
        infoTitle.setFont(FONT_BOLD);

        lblHeight = new JLabel("Height: 0");
        lblHeight.setFont(FONT_NORMAL);

        taTraversals = new JTextArea();
        taTraversals.setEditable(false);
        taTraversals.setFont(new Font("Consolas", Font.PLAIN, 13));
        taTraversals.setLineWrap(true);
        taTraversals.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(taTraversals);

        infoPanel.add(infoTitle, BorderLayout.NORTH);
        infoPanel.add(scroll, BorderLayout.CENTER);
        infoPanel.add(lblHeight, BorderLayout.SOUTH);

        panel.add(treePanel, BorderLayout.CENTER);
        panel.add(infoPanel, BorderLayout.EAST);

        return panel;
    }

    // ================= BOTTOM =================
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(5, 15, 5, 15));

        lblStatus = new JLabel("Ready");
        lblStatus.setFont(FONT_NORMAL);

        panel.add(lblStatus, BorderLayout.WEST);
        return panel;
    }

    // ================= ACTIONS =================
    private void insertAction() {
        Integer value = getInput();
        if (value == null) return;

        searchedValue = null;

        if (!bst.insert(value)) {
            setStatus("Duplicate value not allowed");
            return;
        }

        setStatus("Inserted " + value);
        refreshUI();
    }

    private void deleteAction() {
        Integer value = getInput();
        if (value == null) return;

        searchedValue = null;
        bst.delete(value);

        setStatus("Deleted " + value);
        refreshUI();
    }

    private void searchAction() {
        Integer value = getInput();
        if (value == null) return;

        searchedValue = value;
        boolean found = bst.search(value);

        setStatus(found ? "Value found" : "Value not found");
        treePanel.repaint();

        JOptionPane.showMessageDialog(
                this,
                found ? "Value " + value + " found in the tree"
                        : "Value " + value + " NOT found in the tree",
                "Search Result",
                found ? JOptionPane.INFORMATION_MESSAGE
                        : JOptionPane.WARNING_MESSAGE
        );
    }

    private void clearAction() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Clear the entire tree?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            searchedValue = null;
            bst.clear();
            setStatus("Tree cleared");
            refreshUI();
        }
    }

    // ================= HELPERS =================
    private Integer getInput() {
        try {
            return Integer.parseInt(tf.getText().trim());
        } catch (Exception e) {
            setStatus("Enter a valid integer");
            return null;
        }
    }

    private void refreshUI() {
        taTraversals.setText(
                "Inorder   : " + bst.inorder() + "\n\n" +
                        "Preorder  : " + bst.preorder() + "\n\n" +
                        "Postorder : " + bst.postorder()
        );

        lblHeight.setText("Height: " + bst.height());
        treePanel.repaint();
        tf.setText("");
    }

    private void setStatus(String msg) {
        lblStatus.setText(msg);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_NORMAL);
        btn.setFocusPainted(false);
        return btn;
    }

    // ================= TREE PANEL =================
    class TreePanel extends JPanel {

        private static final int RADIUS = 22;
        private BST bst;

        public TreePanel(BST bst) {
            this.bst = bst;
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (bst.getRoot() != null)
                drawTree(g, bst.getRoot(), getWidth() / 2, 50, getWidth() / 4);
        }

        private void drawTree(Graphics g, BSTNode node, int x, int y, int gap) {
            if (node == null) return;

            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));

            if (searchedValue != null && node.getData() == searchedValue)
                g2.setColor(new Color(255, 182, 193)); // highlight
            else if (node == bst.getRoot())
                g2.setColor(new Color(100, 149, 237)); // root
            else
                g2.setColor(new Color(152, 251, 152)); // normal

            g2.fillOval(x - RADIUS, y - RADIUS, 2 * RADIUS, 2 * RADIUS);
            g2.setColor(Color.DARK_GRAY);
            g2.drawOval(x - RADIUS, y - RADIUS, 2 * RADIUS, 2 * RADIUS);

            String text = String.valueOf(node.getData());
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(text, x - fm.stringWidth(text) / 2,
                    y + fm.getAscent() / 4);

            if (node.getLeft() != null) {
                drawEdge(g2, x, y, x - gap, y + 70);
                drawTree(g, node.getLeft(), x - gap, y + 70, gap / 2);
            }

            if (node.getRight() != null) {
                drawEdge(g2, x, y, x + gap, y + 70);
                drawTree(g, node.getRight(), x + gap, y + 70, gap / 2);
            }
        }

        private void drawEdge(Graphics g, int x1, int y1, int x2, int y2) {
            double angle = Math.atan2(y2 - y1, x2 - x1);
            int sx = (int) (x1 + RADIUS * Math.cos(angle));
            int sy = (int) (y1 + RADIUS * Math.sin(angle));
            int ex = (int) (x2 - RADIUS * Math.cos(angle));
            int ey = (int) (y2 - RADIUS * Math.sin(angle));
            g.drawLine(sx, sy, ex, ey);
        }
    }
}
