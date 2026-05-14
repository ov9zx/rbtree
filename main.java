import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class RedBlackTreeVisualization extends JFrame {
    private RedBlackTree<Integer> tree;
    private TreePanel treePanel;
    private JTextField inputField;
    private JTextArea infoArea;
    
    public RedBlackTreeVisualization() {
        tree = new RedBlackTree<>();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Red-Black Tree - Visualization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Tree drawing panel
        treePanel = new TreePanel();
        JScrollPane scrollPane = new JScrollPane(treePanel);
        scrollPane.setPreferredSize(new Dimension(800, 500));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Control panel
        JPanel controlPanel = createControlPanel();
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        
        // Information panel
        JPanel infoPanel = createInfoPanel();
        mainPanel.add(infoPanel, BorderLayout.EAST);
        
        add(mainPanel);
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Controls"));
        
        // Input field
        inputField = new JTextField(10);
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(new JLabel("Value:"));
        panel.add(inputField);
        
        // Operation buttons
        JButton insertButton = createStyledButton("Insert", new Color(46, 139, 87));
        JButton deleteButton = createStyledButton("Delete", new Color(220, 20, 60));
        JButton searchButton = createStyledButton("Search", new Color(30, 144, 255));
        JButton clearButton = createStyledButton("Clear", new Color(128, 128, 128));
        JButton randomButton = createStyledButton("Random", new Color(255, 140, 0));
        
        insertButton.addActionListener(e -> performInsert());
        deleteButton.addActionListener(e -> performDelete());
        searchButton.addActionListener(e -> performSearch());
        clearButton.addActionListener(e -> performClear());
        randomButton.addActionListener(e -> insertRandom());
        
        panel.add(insertButton);
        panel.add(deleteButton);
        panel.add(searchButton);
        panel.add(randomButton);
        panel.add(clearButton);
        
        return panel;
    }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(250, 500));
        panel.setBorder(BorderFactory.createTitledBorder("Information"));
        
        infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        infoArea.setBackground(new Color(245, 245, 245));
        
        JScrollPane scrollPane = new JScrollPane(infoArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        updateInfo();
        return panel;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void performInsert() {
        try {
            int value = Integer.parseInt(inputField.getText());
            if (!tree.contains(value)) {
                tree.insert(value);
                updateInfo();
                treePanel.repaint();
                inputField.setText("");
                JOptionPane.showMessageDialog(this, "Value " + value + " inserted!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Value " + value + " already exists!", 
                    "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter an integer!", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void performDelete() {
        try {
            int value = Integer.parseInt(inputField.getText());
            if (tree.contains(value)) {
                tree.delete(value);
                updateInfo();
                treePanel.repaint();
                inputField.setText("");
                JOptionPane.showMessageDialog(this, "Value " + value + " deleted!", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Value " + value + " not found!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter an integer!", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void performSearch() {
        try {
            int value = Integer.parseInt(inputField.getText());
            boolean found = tree.contains(value);
            treePanel.highlightValue = found ? value : null;
            treePanel.repaint();
            
            if (found) {
                JOptionPane.showMessageDialog(this, "Value " + value + " found!", 
                    "Search", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Value " + value + " not found!", 
                    "Search", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter an integer!", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void performClear() {
        tree = new RedBlackTree<>();
        treePanel.highlightValue = null;
        updateInfo();
        treePanel.repaint();
    }
    
    private void insertRandom() {
        for (int i = 0; i < 7; i++) {
            int value = (int)(Math.random() * 100);
            if (!tree.contains(value)) {
                tree.insert(value);
            }
        }
        updateInfo();
        treePanel.repaint();
    }
    
    private void updateInfo() {
        List<Integer> values = tree.inorderTraversalList();
        infoArea.setText("=== Tree Information ===\n\n");
        infoArea.append("Number of nodes: " + values.size() + "\n");
        infoArea.append("Valid RBT: " + (tree.isValidRedBlackTree() ? "Yes" : "No") + "\n\n");
        infoArea.append("Elements (inorder):\n");
        
        for (int i = 0; i < values.size(); i++) {
            infoArea.append(values.get(i) + " ");
            if ((i + 1) % 10 == 0) {
                infoArea.append("\n");
            }
        }
        
        infoArea.append("\n\nTree height: " + tree.getHeight());
    }
    
    // Tree drawing panel
    class TreePanel extends JPanel {
        public Integer highlightValue = null;
        private final int NODE_RADIUS = 25;
        private final int LEVEL_HEIGHT = 80;
        
        public TreePanel() {
            setPreferredSize(new Dimension(800, 600));
            setBackground(Color.WHITE);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (tree.root != null) {
                drawTree(g2d, tree.root, getWidth() / 2, 50, getWidth() / 4);
            } else {
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                g2d.setColor(Color.GRAY);
                g2d.drawString("Tree is empty", getWidth() / 2 - 60, getHeight() / 2);
            }
        }
        
        private void drawTree(Graphics2D g2d, RedBlackTree<Integer>.Node node, 
                             int x, int y, int xOffset) {
            if (node == null) return;
            
            // Draw connections
            if (node.left != null) {
                int childX = x - xOffset;
                int childY = y + LEVEL_HEIGHT;
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(2));
                g2d.draw(new Line2D.Double(x, y + NODE_RADIUS, childX, childY));
                drawTree(g2d, node.left, childX, childY, xOffset / 2);
            }
            
            if (node.right != null) {
                int childX = x + xOffset;
                int childY = y + LEVEL_HEIGHT;
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(2));
                g2d.draw(new Line2D.Double(x, y + NODE_RADIUS, childX, childY));
                drawTree(g2d, node.right, childX, childY, xOffset / 2);
            }
            
            // Draw node
            Color nodeColor = node.color == RedBlackTree.RED ? Color.RED : Color.BLACK;
            Ellipse2D.Double circle = new Ellipse2D.Double(
                x - NODE_RADIUS, y, 2 * NODE_RADIUS, 2 * NODE_RADIUS);
            
            // Highlight for search
            if (highlightValue != null && node.data.equals(highlightValue)) {
                g2d.setColor(Color.YELLOW);
                g2d.fill(circle);
                g2d.setColor(nodeColor);
                g2d.setStroke(new BasicStroke(3));
                g2d.draw(circle);
                g2d.setStroke(new BasicStroke(1));
            } else {
                g2d.setColor(nodeColor);
                g2d.fill(circle);
                g2d.setColor(Color.BLACK);
                g2d.draw(circle);
            }
            
            // Draw text
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            FontMetrics fm = g2d.getFontMetrics();
            String text = String.valueOf(node.data);
            int textX = x - fm.stringWidth(text) / 2;
            int textY = y + NODE_RADIUS + fm.getAscent() / 2 - 2;
            
            g2d.setColor(Color.WHITE);
            g2d.drawString(text, textX, textY);
        }
    }
    
    // Red-Black Tree class
    static class RedBlackTree<T extends Comparable<T>> {
        private static final boolean RED = true;
        private static final boolean BLACK = false;
        
        private class Node {
            T data;
            Node left, right, parent;
            boolean color;
            
            Node(T data) {
                this.data = data;
                this.color = RED;
            }
        }
        
        private Node root;
        
        public RedBlackTree() {
            root = null;
        }
        
        public boolean contains(T data) {
            return search(data) != null;
        }
        
        public Node search(T data) {
            return searchRecursive(root, data);
        }
        
        private Node searchRecursive(Node node, T data) {
            if (node == null || node.data.equals(data)) {
                return node;
            }
            
            if (data.compareTo(node.data) < 0) {
                return searchRecursive(node.left, data);
            } else {
                return searchRecursive(node.right, data);
            }
        }
        
        public void insert(T data) {
            Node newNode = new Node(data);
            root = insertNode(root, newNode);
            fixInsert(newNode);
        }
        
        private Node insertNode(Node root, Node newNode) {
            if (root == null) {
                return newNode;
            }
            
            if (newNode.data.compareTo(root.data) < 0) {
                root.left = insertNode(root.left, newNode);
                root.left.parent = root;
            } else if (newNode.data.compareTo(root.data) > 0) {
                root.right = insertNode(root.right, newNode);
                root.right.parent = root;
            }
            
            return root;
        }
        
        private void fixInsert(Node node) {
            Node parent = null;
            Node grandParent = null;
            
            while (node != root && node.color == RED && node.parent.color == RED) {
                parent = node.parent;
                grandParent = parent.parent;
                
                if (parent == grandParent.left) {
                    Node uncle = grandParent.right;
                    
                    if (uncle != null && uncle.color == RED) {
                        grandParent.color = RED;
                        parent.color = BLACK;
                        uncle.color = BLACK;
                        node = grandParent;
                    } else {
                        if (node == parent.right) {
                            rotateLeft(parent);
                            node = parent;
                            parent = node.parent;
                        }
                        
                        rotateRight(grandParent);
                        boolean tempColor = parent.color;
                        parent.color = grandParent.color;
                        grandParent.color = tempColor;
                        node = parent;
                    }
                } else {
                    Node uncle = grandParent.left;
                    
                    if (uncle != null && uncle.color == RED) {
                        grandParent.color = RED;
                        parent.color = BLACK;
                        uncle.color = BLACK;
                        node = grandParent;
                    } else {
                        if (node == parent.left) {
                            rotateRight(parent);
                            node = parent;
                            parent = node.parent;
                        }
                        
                        rotateLeft(grandParent);
                        boolean tempColor = parent.color;
                        parent.color = grandParent.color;
                        grandParent.color = tempColor;
                        node = parent;
                    }
                }
            }
            
            root.color = BLACK;
        }
        
        private void rotateLeft(Node node) {
            Node rightChild = node.right;
            node.right = rightChild.left;
            
            if (node.right != null) {
                node.right.parent = node;
            }
            
            rightChild.parent = node.parent;
            
            if (node.parent == null) {
                root = rightChild;
            } else if (node == node.parent.left) {
                node.parent.left = rightChild;
            } else {
                node.parent.right = rightChild;
            }
            
            rightChild.left = node;
            node.parent = rightChild;
        }
        
        private void rotateRight(Node node) {
            Node leftChild = node.left;
            node.left = leftChild.right;
            
            if (node.left != null) {
                node.left.parent = node;
            }
            
            leftChild.parent = node.parent;
            
            if (node.parent == null) {
                root = leftChild;
            } else if (node == node.parent.left) {
                node.parent.left = leftChild;
            } else {
                node.parent.right = leftChild;
            }
            
            leftChild.right = node;
            node.parent = leftChild;
        }
        
        public void delete(T data) {
            Node node = search(data);
            if (node == null) return;
            deleteNode(node);
        }
        
        private void deleteNode(Node node) {
            Node y = node;
            Node x;
            boolean yOriginalColor = y.color;
            
            if (node.left == null) {
                x = node.right;
                transplant(node, node.right);
            } else if (node.right == null) {
                x = node.left;
                transplant(node, node.left);
            } else {
                y = minimum(node.right);
                yOriginalColor = y.color;
                x = y.right;
                
                if (y.parent == node) {
                    if (x != null) x.parent = y;
                } else {
                    transplant(y, y.right);
                    y.right = node.right;
                    y.right.parent = y;
                }
                
                transplant(node, y);
                y.left = node.left;
                y.left.parent = y;
                y.color = node.color;
            }
            
            if (yOriginalColor == BLACK && x != null) {
                fixDelete(x);
            }
        }
        
        private void fixDelete(Node x) {
            while (x != root && x.color == BLACK) {
                if (x == x.parent.left) {
                    Node w = x.parent.right;
                    
                    if (w.color == RED) {
                        w.color = BLACK;
                        x.parent.color = RED;
                        rotateLeft(x.parent);
                        w = x.parent.right;
                    }
                    
                    if ((w.left == null || w.left.color == BLACK) && 
                        (w.right == null || w.right.color == BLACK)) {
                        w.color = RED;
                        x = x.parent;
                    } else {
                        if (w.right == null || w.right.color == BLACK) {
                            if (w.left != null) w.left.color = BLACK;
                            w.color = RED;
                            rotateRight(w);
                            w = x.parent.right;
                        }
                        
                        w.color = x.parent.color;
                        x.parent.color = BLACK;
                        if (w.right != null) w.right.color = BLACK;
                        rotateLeft(x.parent);
                        x = root;
                    }
                } else {
                    Node w = x.parent.left;
                    
                    if (w.color == RED) {
                        w.color = BLACK;
                        x.parent.color = RED;
                        rotateRight(x.parent);
                        w = x.parent.left;
                    }
                    
                    if ((w.right == null || w.right.color == BLACK) && 
                        (w.left == null || w.left.color == BLACK)) {
                        w.color = RED;
                        x = x.parent;
                    } else {
                        if (w.left == null || w.left.color == BLACK) {
                            if (w.right != null) w.right.color = BLACK;
                            w.color = RED;
                            rotateLeft(w);
                            w = x.parent.left;
                        }
                        
                        w.color = x.parent.color;
                        x.parent.color = BLACK;
                        if (w.left != null) w.left.color = BLACK;
                        rotateRight(x.parent);
                        x = root;
                    }
                }
            }
            x.color = BLACK;
        }
        
        private void transplant(Node u, Node v) {
            if (u.parent == null) {
                root = v;
            } else if (u == u.parent.left) {
                u.parent.left = v;
            } else {
                u.parent.right = v;
            }
            
            if (v != null) v.parent = u.parent;
        }
        
        private Node minimum(Node node) {
            while (node.left != null) node = node.left;
            return node;
        }
        
        public List<T> inorderTraversalList() {
            List<T> result = new ArrayList<>();
            inorderRecursive(root, result);
            return result;
        }
        
        private void inorderRecursive(Node node, List<T> result) {
            if (node != null) {
                inorderRecursive(node.left, result);
                result.add(node.data);
                inorderRecursive(node.right, result);
            }
        }
        
        public int getHeight() {
            return getHeightRecursive(root);
        }
        
        private int getHeightRecursive(Node node) {
            if (node == null) return -1;
            return 1 + Math.max(getHeightRecursive(node.left), 
                               getHeightRecursive(node.right));
        }
        
        public boolean isValidRedBlackTree() {
            if (root == null) return true;
            if (root.color != BLACK) return false;
            return checkRedBlackProperties(root, 0, new int[]{-1});
        }
        
        private boolean checkRedBlackProperties(Node node, int blackCount, 
                                               int[] pathBlackCount) {
            if (node == null) {
                if (pathBlackCount[0] == -1) {
                    pathBlackCount[0] = blackCount;
                } else if (blackCount != pathBlackCount[0]) {
                    return false;
                }
                return true;
            }
            
            if (node.color == RED) {
                if ((node.left != null && node.left.color == RED) ||
                    (node.right != null && node.right.color == RED)) {
                    return false;
                }
            }
            
            if (node.color == BLACK) blackCount++;
            
            return checkRedBlackProperties(node.left, blackCount, pathBlackCount) &&
                   checkRedBlackProperties(node.right, blackCount, pathBlackCount);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RedBlackTreeVisualization frame = new RedBlackTreeVisualization();
            frame.setVisible(true);
        });
    }
}
