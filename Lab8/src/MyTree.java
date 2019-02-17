import java.util.*;

public class MyTree<E extends Number & Comparable<E>> {
    class Node {
        E value;
        Node left;
        Node right;
        Node root;
        Node(E e) {
            value = e;
        }

        Node getLeft() {
            return left;
        }
        Node getRight() {
            return right;
        }
        Node getRoot() {
            return root;
        }

        E getValue() {
            return value;
        }
        void setLeft(Node left) {
            this.left = left;
            if (left != null) {
                left.root = this;
            }
        }
        void setRight(Node right) {
            this.right = right;
            if (right != null) {
                right.root = this;
            }
        }
        void setRoot(Node root) {
            this.root = root;
            if (root != null) {
                root.setChild(this);
            }
        }
        void setChild(Node child) {
            if (child != null) {
                int cmp = child.value.compareTo(value);
                if (cmp < 0) {
                    setLeft(child);
                } else if (cmp > 0) {
                    setRight(child);
                }
            }
        }
    }

    private Node top;
    private int size;

    MyTree() {
        size = 0;
    }

    public void printInorder() {
        traverseInorder(top);
    }
    public void printPreorder() {
        traversePreorder(top);
    }
    public void printPostorder() {
        traversePostorder(top);
    }
    public void addElement(E e) {
        if (top != null) {
            int cmp = e.compareTo(top.getValue());
            if (cmp < 0) {
                addElement(e, top.getLeft(), top);
            }
            else if (cmp > 0) {
                addElement(e, top.getRight(), top);
            }
        }
        else {
            top = new Node(e);
            size++;
        }
    }
    public void removeElement(E e) {
        Node nodeToRemove = findNode(e, top);
        if (nodeToRemove == null) {
            throw new RuntimeException("Trying to remove non-present value!");
        }
        Node leftTreeTop = nodeToRemove.getLeft();
        Node rightTreeTop = nodeToRemove.getRight();
        Node parent = nodeToRemove.getRoot();
        if (leftTreeTop != null) {
            Node maxOfLeftTree = findMaxNode(leftTreeTop);
            if (maxOfLeftTree == leftTreeTop) {
                leftTreeTop.setRight(rightTreeTop);
                leftTreeTop.setRoot(parent);
            }
            else {
                maxOfLeftTree.getRoot().setRight(maxOfLeftTree.getLeft());
                maxOfLeftTree.setLeft(leftTreeTop);
                maxOfLeftTree.setRight(rightTreeTop);
            }
            if (parent != null) parent.setChild(maxOfLeftTree);
            else top = maxOfLeftTree;
        }
        else if (rightTreeTop != null) {
            rightTreeTop.setRoot(parent);
            if (parent != null) parent.setChild(rightTreeTop);
            else top = rightTreeTop;
        }
        else if (parent != null && parent.getLeft() == nodeToRemove) {
            parent.setLeft(null);
        }
        else if (parent != null && parent.getRight() == nodeToRemove) {
            parent.setRight(null);
        }
        else {
            top = null;
        }
        size--;
    }
    public boolean isPresent(E e) {
        if (top == null) return false;
        return isPresent(e, top);
    }
    public int getSize() {
        return size;
    }
    public static MyTree<Integer> parseMyTreeInt(String str) {
        int[] data = Arrays.stream(str.split(" ")).map(String::trim).mapToInt(Integer::parseInt).toArray();
        MyTree<Integer> tree = new MyTree<>();
        for (Integer element : data) {
            tree.addElement(element);
        }
        return tree;
    }
    private Node findNode(E e, Node node) {
        if (node == null) return null;
        int cmp = e.compareTo(node.getValue());
        if (cmp > 0) {
            return findNode(e, node.getRight());
        }
        else if (cmp < 0) {
            return findNode(e, node.getLeft());
        }
        else return node;
    }
    private Node findMaxNode(Node node) {
        if (node.getRight() != null) return findMaxNode(node.getRight());
        return node;
    }
    private boolean isPresent(E e, Node node) {
        if (node == null) return false;
        int cmp = e.compareTo(node.getValue());
        if (cmp > 0) {
            return isPresent(e, node.getRight());
        }
        else if (cmp < 0) {
            return isPresent(e, node.getLeft());
        }
        else return true;
    }
    private void addElement(E e, Node node, Node prevNode) {
        if (node != null) {
            int cmp = e.compareTo(node.getValue());
            if (cmp < 0) {
                addElement(e, node.getLeft(), node);
            }
            else if (cmp > 0) {
                addElement(e, node.getRight(), node);
            }
        }
        else {
            prevNode.setChild(new Node(e));
            size++;
        }
    }
    private void traverseInorder(Node node) {
        if (node == null) return;
        if (node.getLeft() != null) {
            traverseInorder(node.getLeft());
        }
        System.out.print(node.getValue() + " ");
        if (node.getRight() != null) {
            traverseInorder(node.getRight());
        }
    }
    private void traversePreorder(Node node) {
        if (node == null) return;
        System.out.print(node.getValue() + " ");
        if (node.getLeft() != null) {
            traversePreorder(node.getLeft());
        }
        if (node.getRight() != null) {
            traversePreorder(node.getRight());
        }
    }
    private void traversePostorder(Node node) {
        if (node == null) return;
        if (node.getLeft() != null) {
            traversePostorder(node.getLeft());
        }
        if (node.getRight() != null) {
            traversePostorder(node.getRight());
        }
        System.out.print(node.getValue() + " ");
    }
}
