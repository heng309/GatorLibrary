/*Red black tree that can search, insert, delete, rotate and count color flip.*/

import java.util.*;

public class GatorRedBlackTree {
    private GatorTreeNode root;
    private final GatorTreeNode NULL;
    private int flipCount;
    // 1 -> RED, 0 -> BLACK
    GatorRedBlackTree() {
        this.flipCount = 0;
        this.NULL = new GatorTreeNode(null);
        this.NULL.color = 0;
        this.NULL.left = null;
        this.NULL.right = null;
        this.root = NULL;
    }

    //    Search books that are closest to target bookID, use the smallest distance to track closest bookIDs.
    public List<Integer> findClosestNode(int bookID) {
        List<Integer> list = new ArrayList<>();
        GatorTreeNode bookNode = root;
        int min = Integer.MAX_VALUE;
        while (bookNode != NULL) {
            int distance = Math.abs(bookID - bookNode.book.BookID);
            if (bookID == bookNode.book.BookID) {
                list.clear();
                list.add(bookID);
                break;
            }
            else {
                if (distance == min) {
                    list.add(bookNode.book.BookID);
                }
                else if (distance < min) {
                    min = distance;
                    list.clear();
                    list.add(bookNode.book.BookID);
                }
            }
            if (bookID < bookNode.book.BookID) {
                bookNode = bookNode.left;
            }
            else {
                bookNode = bookNode.right;
            }
        }
        return list;
    }

    /*
     * Fetch NULL object for following comparisons.
     */
    public GatorTreeNode getNull() {
        return NULL;
    }


    /*
     * Find books in the given bookID interval.
     */
    public List<GatorBook> getBooksInInterval(int bookID1, int bookID2) {
        List<GatorBook> books = new ArrayList<>();
        if (root == NULL) {
            return books;
        }

        // Perform inorder traversal of the RBT
        visitInorder(books, root, bookID1, bookID2);

        return books;
    }

    private void visitInorder(List<GatorBook> bookList, GatorTreeNode bookNode, int min, int max) {
        if (bookNode == NULL) {
            return;
        }

        // Recursively visit left subtree if bookNode's bookID is greater than min
        if (bookNode.book.BookID > min) {
            visitInorder(bookList, bookNode.left, min, max);
        }

        // Add bookNode's book to the bookList if its bookID falls within the given interval
        if (bookNode.book.BookID >= min && bookNode.book.BookID <= max) {
            bookList.add(bookNode.book);
        }

        // Recursively visit right subtree if bookNode's bookID is less than max
        if (bookNode.book.BookID < max) {
            visitInorder(bookList, bookNode.right, min, max);
        }
    }

    /*
     * Insert a book node
     */
    public void insert(GatorTreeNode bookNode) {
        // Ordinary Binary Search Insertion
        bookNode.left = NULL;
        bookNode.right = NULL;
        // New bookNode must be red
        bookNode.color = 1;

        GatorTreeNode newParentNode = null;
        GatorTreeNode tmpBookNode = this.root;

        while (tmpBookNode != NULL) {
            newParentNode = tmpBookNode;
            if (bookNode.book.BookID < tmpBookNode.book.BookID) {
                tmpBookNode = tmpBookNode.left;
            } else {
                tmpBookNode = tmpBookNode.right;
            }
        }

        // newParentNode is parent of tmpBookNode
        bookNode.parent = newParentNode;
        if (newParentNode == null) {
            root = bookNode;
        } else if (bookNode.book.BookID < newParentNode.book.BookID) {
            newParentNode.left = bookNode;
        } else {
            newParentNode.right = bookNode;
        }

        // If new bookNode is a root node, simply return
        if (bookNode.parent == null) {
            bookNode.color = 0;
            return;
        }

        // if the grandparent is null, simply return
        if (bookNode.parent.parent == null) {
            return;
        }

        // Fix the tree after insertion.
        fixInsert(bookNode);
    }

    private void fixInsert(GatorTreeNode bookNode){
        GatorTreeNode uncleNode;
        while (bookNode.parent.color == 1) {
            if (bookNode.parent == bookNode.parent.parent.right) {
                uncleNode = bookNode.parent.parent.left;
                if (uncleNode.color == 1) {
                    // parent red, uncleNode red
                    flipCount += 2;
                    uncleNode.color = 0;
                    bookNode.parent.color = 0;
                    if (bookNode.parent.parent.color == 0) flipCount++;
                    bookNode.parent.parent.color = 1;
                    bookNode = bookNode.parent.parent;
                } else {
                    if (bookNode == bookNode.parent.left) {
                        // parent is right child, node is left child
                        bookNode = bookNode.parent;
                        rightRotate(bookNode);
                    }
                    // parent is right child, node is right child
                    flipCount++;
                    bookNode.parent.color = 0;
                    if (bookNode.parent.parent.color == 0) flipCount++;
                    bookNode.parent.parent.color = 1;
                    leftRotate(bookNode.parent.parent);
                }
            } else {
                uncleNode = bookNode.parent.parent.right;

                if (uncleNode.color == 1) {
                    // parent is black, uncleNode is black
                    flipCount += 2;
                    uncleNode.color = 0;
                    bookNode.parent.color = 0;
                    if (bookNode.parent.parent.color == 0) flipCount++;
                    bookNode.parent.parent.color = 1;
                    bookNode = bookNode.parent.parent;
                } else {
                    if (bookNode == bookNode.parent.right) {
                        // parent is left child, node is right child
                        bookNode = bookNode.parent;
                        leftRotate(bookNode);
                    }
                    // parent is left child, node is left child
                    flipCount++;
                    bookNode.parent.color = 0;
                    if (bookNode.parent.parent.color == 0) flipCount++;
                    bookNode.parent.parent.color = 1;
                    rightRotate(bookNode.parent.parent);
                }
            }
            if (bookNode == root) {
                break;
            }
        }
        if (root.color == 1) flipCount++;
        root.color = 0;
    }


    /*
     * Delete a book node
     */
    public void deleteNode(int bookID) {
        GatorTreeNode node = root;
        GatorTreeNode targetBookNode = NULL;
        GatorTreeNode tmpChildBookNode, tmpBookNode;
        while (node != NULL){
            if (node.book.BookID == bookID) {
                targetBookNode = node;
            }
            if (node.book.BookID <= bookID) {
                node = node.right;
            } else {
                node = node.left;
            }
        }

        tmpBookNode = targetBookNode;
        int tmpNodeOriginalColor = tmpBookNode.color;
        if (targetBookNode.left == NULL) {
            tmpChildBookNode = targetBookNode.right;
            replace(targetBookNode, targetBookNode.right);
        } else if (targetBookNode.right == NULL) {
            tmpChildBookNode = targetBookNode.left;
            replace(targetBookNode, targetBookNode.left);
        } else {
            tmpBookNode = inorderSuccessOf(targetBookNode.right);
            tmpNodeOriginalColor = tmpBookNode.color;
            tmpChildBookNode = tmpBookNode.right;
            if (tmpBookNode.parent == targetBookNode) {
                tmpChildBookNode.parent = tmpBookNode;
            } else {
                replace(tmpBookNode, tmpBookNode.right);
                tmpBookNode.right = targetBookNode.right;
                tmpBookNode.right.parent = tmpBookNode;
            }

            replace(targetBookNode, tmpBookNode);
            tmpBookNode.left = targetBookNode.left;
            tmpBookNode.left.parent = tmpBookNode;
            tmpBookNode.color = targetBookNode.color;
        }
        if (tmpNodeOriginalColor == 0){
            fixDelete(tmpChildBookNode);
        }
    }

    private void replace(GatorTreeNode oldBookNode, GatorTreeNode newBookNode){
        if (oldBookNode.parent == null) {
            root = newBookNode;
        } else if (oldBookNode == oldBookNode.parent.left){
            oldBookNode.parent.left = newBookNode;
        } else {
            oldBookNode.parent.right = newBookNode;
        }
        newBookNode.parent = oldBookNode.parent;
    }

//    Fix tree after deletion.
    private void fixDelete(GatorTreeNode bookNodeToFix) {
        GatorTreeNode siblingNode;
        while (bookNodeToFix != root && bookNodeToFix.color == 0) {
            if (bookNodeToFix == bookNodeToFix.parent.left) {
                siblingNode = bookNodeToFix.parent.right;
                if (siblingNode.color == 1) {
                    // siblingNode is red
                    flipCount++;
                    siblingNode.color = 0;
                    if (bookNodeToFix.parent.color == 0) flipCount++;
                    bookNodeToFix.parent.color = 1;
                    leftRotate(bookNodeToFix.parent);
                    siblingNode = bookNodeToFix.parent.right;
                }

                if (siblingNode.left.color == 0 && siblingNode.right.color == 0) {
                    // siblingNode and its children are all black
                    if (siblingNode.color == 0) flipCount++;
                    siblingNode.color = 1;
                    bookNodeToFix = bookNodeToFix.parent;
                } else {
                    if (siblingNode.right.color == 0) {
                        // siblingNode black, leftChild red, rightChild black
                        if (siblingNode.left.color == 1) flipCount++;
                        siblingNode.left.color = 0;
                        if (siblingNode.color == 0) flipCount++;
                        siblingNode.color = 1;
                        rightRotate(siblingNode);
                        siblingNode = bookNodeToFix.parent.right;
                    }

                    // siblingNode black, rightChild red
                    siblingNode.color = bookNodeToFix.parent.color;
                    if (bookNodeToFix.parent.color == 1) flipCount++;
                    bookNodeToFix.parent.color = 0;
                    if (siblingNode.right.color == 1) flipCount++;
                    siblingNode.right.color = 0;
                    leftRotate(bookNodeToFix.parent);
                    bookNodeToFix = root;
                }
            } else {
                siblingNode = bookNodeToFix.parent.left;
                if (siblingNode.color == 1) {
                    // siblingNode is red
                    flipCount++;
                    siblingNode.color = 0;
                    if (bookNodeToFix.parent.color == 0) flipCount++;
                    bookNodeToFix.parent.color = 1;
                    rightRotate(bookNodeToFix.parent);
                    siblingNode = bookNodeToFix.parent.left;
                }

                if (siblingNode.left.color == 0 && siblingNode.right.color == 0) {
                    // siblingNode and its children are all black
                    if (siblingNode.color == 0) flipCount++;
                    siblingNode.color = 1;
                    bookNodeToFix = bookNodeToFix.parent;
                } else {
                    if (siblingNode.left.color == 0) {
                        // siblingNode black, leftChild red, rightChild black
                        if (siblingNode.right.color == 1) flipCount++;
                        siblingNode.right.color = 0;
                        if (siblingNode.color == 0) flipCount++;
                        siblingNode.color = 1;
                        leftRotate(siblingNode);
                        siblingNode = bookNodeToFix.parent.left;
                    }

                    // siblingNode black, rightChild red
                    siblingNode.color = bookNodeToFix.parent.color;
                    if (bookNodeToFix.parent.color == 1) flipCount++;
                    bookNodeToFix.parent.color = 0;
                    if (siblingNode.left.color == 1) flipCount++;
                    siblingNode.left.color = 0;
                    rightRotate(bookNodeToFix.parent);
                    bookNodeToFix = root;
                }
            }
        }
        if (bookNodeToFix.color == 1) flipCount++;
        bookNodeToFix.color = 0;
    }

    private GatorTreeNode inorderSuccessOf(GatorTreeNode bookNode) {
        while (bookNode.left != NULL) {
            bookNode = bookNode.left;
        }
        return bookNode;
    }


    /*
     * Search a node in RBT
     */
    public GatorTreeNode searchBook(int bookID) {
        GatorTreeNode bookNode = root;
        while (bookNode != NULL) {
            if (bookID == bookNode.book.BookID) {
                return bookNode;
            } else if (bookID < bookNode.book.BookID) {
                bookNode = bookNode.left;
            } else {
                bookNode = bookNode.right;
            }
        }

        return NULL;
    }

    public int getFlipCount() {
        return flipCount;
    }


//    Left rotation
    private void leftRotate(GatorTreeNode bookNode) {
        GatorTreeNode rightChildNode = bookNode.right;
        bookNode.right = rightChildNode.left;
        if (rightChildNode.left != NULL) {
            rightChildNode.left.parent = bookNode;
        }
        rightChildNode.parent = bookNode.parent;
        if (bookNode.parent == null) {
            this.root = rightChildNode;
        } else if (bookNode == bookNode.parent.left) {
            bookNode.parent.left = rightChildNode;
        } else {
            bookNode.parent.right = rightChildNode;
        }
        rightChildNode.left = bookNode;
        bookNode.parent = rightChildNode;
    }

//    Right rotation
    private void rightRotate(GatorTreeNode bookNode) {
        GatorTreeNode leftChildNode = bookNode.left;
        bookNode.left = leftChildNode.right;
        if (leftChildNode.right != NULL) {
            leftChildNode.right.parent = bookNode;
        }
        leftChildNode.parent = bookNode.parent;
        if (bookNode.parent == null) {
            this.root = leftChildNode;
        } else if (bookNode == bookNode.parent.right) {
            bookNode.parent.right = leftChildNode;
        } else {
            bookNode.parent.left = leftChildNode;
        }
        leftChildNode.right = bookNode;
        bookNode.parent = leftChildNode;
    }
}
