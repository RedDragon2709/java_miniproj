package bst;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BSTView extends JFrame implements ActionListener {

    private JTextField tf;
    private JButton btnAdd, btnDelete;
    private JLabel lblInorder, lblPreorder, lblPostorder, lblHeight;
    private TreePanel treePanel;

    private BST bst;

    public BSTView(BST bst) {
        this.bst = bst;
        initUI();
    }

    private void initUI() {
        setTitle("BST Visualization");
        setSize(950, 650);
        setLayout(new BorderLayout());

        // ---------- TOP PANEL ----------
        JPanel top = new JPanel();
        tf = new JTextField(10);

        btnAdd = new JButton("Add");
        btnDelete = new JButton("Delete");

        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnAdd.addActionListener(this);
        btnDelete.addActionListener(this);

        top.add(new JLabel("Value: "));
        top.add(tf);
        top.add(btnAdd);
        top.add(btnDelete);

        // ---------- INFO PANEL ----------
        JPanel info = new JPanel(new GridLayout(4, 1, 6, 10));

        Font traversalFont = new Font("Segoe UI", Font.BOLD, 16);

        lblInorder = new JLabel("Inorder: ");
        lblInorder.setFont(traversalFont);

        lblPreorder = new JLabel("Preorder: ");
        lblPreorder.setFont(traversalFont);

        lblPostorder = new JLabel("Postorder: ");
        lblPostorder.setFont(traversalFont);

        lblHeight = new JLabel("Height: 0");
        lblHeight.setFont(new Font("Segoe UI", Font.BOLD, 15));

        info.add(lblInorder);
        info.add(lblPreorder);
        info.add(lblPostorder);
        info.add(lblHeight);

        // ---------- TREE PANEL ----------
        treePanel = new TreePanel(bst);

        add(top, BorderLayout.NORTH);
        add(treePanel, BorderLayout.CENTER);
        add(info, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int value = Integer.parseInt(tf.getText());

            if (e.getSource() == btnAdd)
                bst.insert(value);
            else
                bst.delete(value);

            lblInorder.setText("Inorder: " + bst.inorder());
            lblPreorder.setText("Preorder: " + bst.preorder());
            lblPostorder.setText("Postorder: " + bst.postorder());
            lblHeight.setText("Height: " + bst.height());

            treePanel.repaint();
            tf.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid integer");
        }
    }

    // ================= TREE DRAWING PANEL =================
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

            if (bst.getRoot() != null) {
                drawTree(g, bst.getRoot(),
                        getWidth() / 2,
                        50,
                        getWidth() / 4);
            }
        }

        private void drawTree(Graphics g, BSTNode node, int x, int y, int gap) {
            if (node == null) return;

            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));

            // ---- Draw node ----
            g2.setColor(new Color(144, 238, 144)); // light green
            g2.fillOval(x - RADIUS, y - RADIUS, 2 * RADIUS, 2 * RADIUS);

            g2.setColor(Color.BLACK);
            g2.drawOval(x - RADIUS, y - RADIUS, 2 * RADIUS, 2 * RADIUS);

            // ---- Node text (centered & bigger) ----
            g2.setFont(new Font("Arial", Font.BOLD, 15));
            String text = String.valueOf(node.data);
            FontMetrics fm = g2.getFontMetrics();

            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getAscent();

            g2.drawString(text, x - textWidth / 2, y + textHeight / 4);

            // ---- Left child ----
            if (node.left != null) {
                int cx = x - gap;
                int cy = y + 70;
                drawEdge(g2, x, y, cx, cy);
                drawTree(g2, node.left, cx, cy, gap / 2);
            }

            // ---- Right child ----
            if (node.right != null) {
                int cx = x + gap;
                int cy = y + 70;
                drawEdge(g2, x, y, cx, cy);
                drawTree(g2, node.right, cx, cy, gap / 2);
            }
        }

        // Draw line from circumference to circumference
        private void drawEdge(Graphics g, int x1, int y1, int x2, int y2) {
            double angle = Math.atan2(y2 - y1, x2 - x1);

            int startX = (int) (x1 + RADIUS * Math.cos(angle));
            int startY = (int) (y1 + RADIUS * Math.sin(angle));

            int endX = (int) (x2 - RADIUS * Math.cos(angle));
            int endY = (int) (y2 - RADIUS * Math.sin(angle));

            g.drawLine(startX, startY, endX, endY);
        }
    }
}
