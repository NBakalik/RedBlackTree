package com.company;

import static com.company.Color.*;

public class RedBlackTree {
    Node root = null;

    public Node search(int value) {
        Node node = root;
        while (node != null) {
            if (value == node.data) {
                return node;
            } else if (value < node.data) {
                node = node.left;
            } else {
                node = node.right;
            }
        }
        return null;
    }

    private void rotateRight(Node node) {
        Node parent = node.parent;
        Node leftChild = node.left;

        node.left = leftChild.right;
        if (leftChild.right != null) {
            leftChild.right.parent = node;
        }

        leftChild.right = node;
        node.parent = leftChild;

        changeParentsChild(parent, node, leftChild);
    }

    private void rotateLeft(Node node) {
        Node parent = node.parent;
        Node rightChild = node.right;

        node.right = rightChild.left;
        if (rightChild.left != null) {
            rightChild.left.parent = node;
        }

        rightChild.left = node;
        node.parent = rightChild;

        changeParentsChild(parent, node, rightChild);
    }

    private void changeParentsChild(Node parent, Node oldChild, Node newChild) {
        if (parent == null) {
            root = newChild;
        } else if (parent.left == oldChild) {
            parent.left = newChild;
        } else if (parent.right == oldChild) {
            parent.right = newChild;
        }

        if (newChild != null) {
            newChild.parent = parent;
        }
    }

    public void insertNode(int value) {
        Node node = root;
        Node parent = null;

        while (node != null) {
            parent = node;
            if (value < node.data) {
                node = node.left;
            } else if (value > node.data) {
                node = node.right;
            }
        }

        Node newNode = new Node(value);
        newNode.color = RED;
        if (parent == null) {
            root = newNode;
        } else if (value < parent.data) {
            parent.left = newNode;
        } else {
            parent.right = newNode;
        }
        newNode.parent = parent;

        fixTreeAfterInsert(newNode);
    }

    private void fixTreeAfterInsert(Node node) {
        Node parent = node.parent;
//        Case 1: New node is the root
//        Case 2: Parent and uncle nodes are red
//        Case 3: Parent node is red, uncle node is black, inserted node is "inner grandchild"
//        Case 4: Parent node is red, uncle node is black, inserted node is "outer grandchild"

        // Case 1: Parent is null, we've reached the root, the end of the recursion
        if (parent == null) {
            node.color = BLACK;
            return;
        }

        if (parent.color == BLACK) {
            return;
        }

        Node grandparent = parent.parent;

        Node uncle = getUncle(parent);

        // Case 2: Uncle is red -> recolor parent, grandparent and uncle
        if (uncle != null && uncle.color == RED) {
            parent.color = BLACK;
            grandparent.color = RED;
            uncle.color = BLACK;

            fixTreeAfterInsert(grandparent);
        }

        // Parent is left child of grandparent
        else if (parent == grandparent.left) {
            // Case 3a: Uncle is black and node is left->right "inner child" of its grandparent
            if (node == parent.right) {
                rotateLeft(parent);
                parent = node;
            }

            // Case 4a: Uncle is black and node is left->left "outer child" of its grandparent
            rotateRight(grandparent);

            parent.color = BLACK;
            grandparent.color = RED;
        }

        // Parent is right child of grandparent
        else {
            // Case 3b: Uncle is black and node is right->left "inner child" of its grandparent
            if (node == parent.left) {
                rotateRight(parent);
                parent = node;
            }

            // Case 4b: Uncle is black and node is right->right "outer child" of its grandparent
            rotateLeft(grandparent);
            parent.color = BLACK;
            grandparent.color = RED;
        }
    }

    private Node getUncle(Node parent) {
        Node grandparent = parent.parent;
        if (grandparent.left == parent) {
            return grandparent.right;
        } else {
            return grandparent.left;
        }
    }
}
