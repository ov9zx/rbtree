import javax.swing.*;
import java.awt.*;

class RBNode {
    int data;
    RBNode parent, left, right;
    int color; // 1 🔴 (Red), 0 ⚫ (Black)

    public RBNode(int data) {
        this.data = data;
        this.color = 1; 
    }
}

class RedBlackTree {
    private RBNode root;
    private final RBNode TNULL;

    public RedBlackTree() {
        TNULL = new RBNode(0);
        TNULL.color = 0;
        TNULL.left = null;
        TNULL.right = null;
        root = TNULL;
    }

    public RBNode getRoot() {
        return this.root;
    }

    public RBNode getTNULL() {
        return this.TNULL;
    }

    // 🔍 Search (Поиск)
    public RBNode searchTree(int k) {
        return searchTreeHelper(this.root, k);
    }

    private RBNode searchTreeHelper(RBNode node, int key) {
        if (node == TNULL || key == node.data) {
            return node;
        }
        if (key < node.data) {
            return searchTreeHelper(node.left, key);
        }
        return searchTreeHelper(node.right, key);
    }

    // ➕ Insert (Вставка элемента)
    public void insert(int key) {
        RBNode node = new RBNode(key);
        node.parent = null;
        node.data = key;
        node.left = TNULL;
        node.right = TNULL;
        node.color = 1; // 🔴 New node is always red

        RBNode y = null;
        RBNode x = this.root;

        while (x != TNULL) {
            y = x;
            if (node.data < x.data) {
                x = x.left;
            } else {
                x = x.right;
            }
        }

        node.parent = y;
        if (y == null) {
            root = node;
        } else if (node.data < y.data) {
            y.left = node;
        } else {
            y.right = node;
        }

        if (node.parent == null) {
            node.color = 0;
            return;
        }
        if (node.parent.parent == null) {
            return;
        }
        insertFix(node);
    }

    private void insertFix(RBNode k) {
        RBNode u;
        while (k.parent.color == 1) {
            if (k.parent == k.parent.parent.right) {
                u = k.parent.parent.left;
                if (u.color == 1) {
                    u.color = 0;
                    k.parent.color = 0;
                    k.parent.parent.color = 1;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.left) {
                        k = k.parent;
                        rightRotate(k);
                    }
                    k.parent.color = 0;
                    k.parent.parent.color = 1;
                    leftRotate(k.parent.parent);
                }
            } else {
                u = k.parent.parent.right;
                if (u.color == 1) {
                    u.color = 0;
                    k.parent.color = 0;
                    k.parent.parent.color = 1;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.right) {
                        k = k.parent;
                        leftRotate(k);
                    }
                    k.parent.color = 0;
                    k.parent.parent.color = 1;
                    rightRotate(k.parent.parent);
                }
            }
            if (k == root) {
                break;
            }
        }
        root.color = 0;
    }

    // ➖ Delete (Удаление элемента)
    public void deleteNode(int data) {
        deleteNodeHelper(this.root, data);
    }

    private void deleteNodeHelper(RBNode node, int key) {
        RBNode z = TNULL;
        RBNode x, y;
        while (node != TNULL) {
            if (node.data == key) {
                z = node;
            }
            if (node.data <= key) {
                node = node.right;
            } else {
                node = node.left;
            }
        }

        if (z == TNULL) {
            return; // Key not found
        }

        y = z;
        int yOriginalColor = y.color;
        if (z.left == TNULL) {
            x = z.right;
            rbTransplant(z, z.right);
        } else if (z.right == TNULL) {
            x = z.left;
            rbTransplant(z, z.left);
        } else {
            y = minimum(z.right);
            yOriginalColor = y.color;
            x = y.right;
            if (y.parent == z) {
                x.parent = y;
            } else {
                rbTransplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            rbTransplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color;
        }
        if (yOriginalColor == 0) {
            deleteFix(x);
        }
    }

    private void deleteFix(RBNode x) {
        RBNode s;
        while (x != root && x.color == 0) {
            if (x == x.parent.left) {
                s = x.parent.right;
                if (s.color == 1) {
                    s.color = 0;
                    x.parent.color = 1;
                    leftRotate(x.parent);
                    s = x.parent.right;
                }
                if (s.left.color == 0 && s.right.color == 0) {
                    s.color = 1;
                    x = x.parent;
                } else {
                    if (s.right.color == 0) {
                        s.left.color = 0;
                        s.color = 1;
                        rightRotate(s);
                        s = x.parent.right;
                    }
                    s.color = x.parent.color;
                    x.parent.color = 0;
                    s.right.color = 0;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                s = x.parent.left;
                if (s.color == 1) {
                    s.color = 0;
                    x.parent.color = 1;
                    rightRotate(x.parent);
                    s = x.parent.left;
                }
                if (s.right.color == 0 && s.left.color == 0) {
                    s.color = 1;
                    x = x.parent;
                } else {
                    if (s.left.color == 0) {
                        s.right.color = 0;
                        s.color = 1;
                        leftRotate(s);
                        s = x.parent.left;
                    }
                    s.color = x.parent.color;
                    x.parent.color = 0;
                    s.left.color = 0;
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        x.color = 0;
    }

    private void rbTransplant(RBNode u, RBNode v) {
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    private RBNode minimum(RBNode node) {
        while (node.left != TNULL) {
            node = node.left;
        }
        return node;
    }

    private void leftRotate(RBNode x) {
        RBNode y = x.right;
        x.right = y.left;
        if (y.left != TNULL) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
    }

    private void rightRotate(RBNode x) {
        RBNode y = x.left;
        x.left = y.right;
        if (y.right != TNULL) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
    }
}

public class RBTVisualizer extends JFrame {
    private RedBlackTree tree = new RedBlackTree();
    private TreePanel treePanel = new TreePanel();
    private JTextField inputField = new JTextField(10);
    private JLabel statusLabel = new JLabel("Status: Waiting for input...");

    public RBTVisualizer() {
        setTitle("Red-Black Tree Visualizer 🌳");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        JButton insertBtn = new JButton("Insert ➕");
        JButton deleteBtn = new JButton("Delete ➖");
        JButton searchBtn = new JButton("Search 🔍");

        controlPanel.add(new JLabel("Value:"));
        controlPanel.add(inputField);
        controlPanel.add(insertBtn);
        controlPanel.add(deleteBtn);
        controlPanel.add(searchBtn);
        
        add(controlPanel, BorderLayout.NORTH);
        add(treePanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        insertBtn.addActionListener(e -> {
            try {
                int val = Integer.parseInt(inputField.getText());
                tree.insert(val);
                statusLabel.setText("Status: Inserted " + val + " ✔️");
                treePanel.repaint();
            } catch (NumberFormatException ex) {
                statusLabel.setText("Status: Invalid input ❌");
            }
        });

        deleteBtn.addActionListener(e -> {
            try {
                int val = Integer.parseInt(inputField.getText());
                tree.deleteNode(val);
                statusLabel.setText("Status: Deleted " + val + " ✔️");
                treePanel.repaint();
            } catch (NumberFormatException ex) {
                statusLabel.setText("Status: Invalid input ❌");
            }
        });

        searchBtn.addActionListener(e -> {
            try {
                int val = Integer.parseInt(inputField.getText());
                RBNode result = tree.searchTree(val);
                if (result != tree.getTNULL()) {
                    statusLabel.setText("Status: Found " + val + " 🔍");
                } else {
                    statusLabel.setText("Status: " + val + " not found ❌");
                }
            } catch (NumberFormatException ex) {
                statusLabel.setText("Status: Invalid input ❌");
            }
        });
    }

    class TreePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (tree.getRoot() != tree.getTNULL()) {
                drawNode(g, tree.getRoot(), getWidth() / 2, 30, getWidth() / 4);
            }
        }

        private void drawNode(Graphics g, RBNode node, int x, int y, int xOffset) {
            if (node == tree.getTNULL()) return;

            g.setFont(new Font("Arial", Font.BOLD, 14));
            
            if (node.left != tree.getTNULL()) {
                g.setColor(Color.BLACK);
                g.drawLine(x, y, x - xOffset, y + 50);
                drawNode(g, node.left, x - xOffset, y + 50, xOffset / 2);
            }
            if (node.right != tree.getTNULL()) {
                g.setColor(Color.BLACK);
                g.drawLine(x, y, x + xOffset, y + 50);
                drawNode(g, node.right, x + xOffset, y + 50, xOffset / 2);
            }

            if (node.color == 1) {
                g.setColor(Color.RED); 
            } else {
                g.setColor(Color.BLACK); 
            }
            
            g.fillOval(x - 15, y - 15, 30, 30);
            g.setColor(Color.WHITE);
            String val = String.valueOf(node.data);
            g.drawString(val, x - g.getFontMetrics().stringWidth(val) / 2, y + 5);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RBTVisualizer().setVisible(true);
        });
    }
}
