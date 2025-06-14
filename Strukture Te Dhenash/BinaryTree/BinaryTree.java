package BinaryTree;

class BinaryNode<AnyType extends Comparable<AnyType>> {
    public AnyType element;
    public BinaryNode<AnyType> left;
    public BinaryNode<AnyType> right;

    public BinaryNode() {
        this(null, null, null);
    }

    public BinaryNode(AnyType el) {
        this(el, null, null);
    }

    public BinaryNode(AnyType el, BinaryNode<AnyType> l, BinaryNode<AnyType> r) {
        element = el;
        right = r;
        left = l;
    }
}

public class BinaryTree<AnyType extends Comparable<AnyType>> {

    public BinaryNode<AnyType> root;

    public BinaryTree() {
        root = null;
    }

    public BinaryTree(AnyType r) {
        root = new BinaryNode<AnyType>(r, null, null);
    }

    public BinaryTree(BinaryNode<AnyType> r) {
        root = r;
    }

    public BinaryTree(AnyType[] array) {
        root = VektortoTree(array);
    }

    public void makeEmpty() {
        root = null;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int height(BinaryNode<AnyType> t) {
        if (t == null) return -1;
        else return 1 + Math.max(height(t.left), height(t.right));
    }

    public int depth(BinaryNode<AnyType> node) {
        if (node == null) return -1;
        BinaryNode<AnyType> current = root;
        int depth = 0;

        while (current != node) {
            if (node.element.compareTo(current.element) < 0) current = current.left;
            else current = current.right;
            depth++;
        }
        return depth;
    }

    public int depth(BinaryNode<AnyType> current, BinaryNode<AnyType> node) {
        if (node == null) return -1;
        if (current == node) return 0;

        if (node.element.compareTo(current.element) < 0) return 1 + depth(current.left, node);
        else return 1 + depth(current.right, node);
    }

    public void printInorder(BinaryNode<Integer> node) {
        if (node == null) return;
        printInorder(node.left);
        System.out.print(node.element + " ");
        printInorder(node.right);
    }

    public void printPreorder(BinaryNode<Integer> node) {
        if (node == null) return;
        System.out.print(node.element + " ");
        printPreorder(node.left);
        printPreorder(node.right);
    }

    public void printPostorder(BinaryNode<Integer> node) {
        if (node == null) return;
        printPostorder(node.left);
        printPostorder(node.right);
        System.out.print(node.element + " ");
    }

    public void printEven(BinaryNode<Integer> node) {
        if (node == null) return;
        if (node.element % 2 == 0) System.out.print(node.element + " ");
        printEven(node.left);
        printEven(node.right);
    }

    public void printOdd(BinaryNode<Integer> node) {
        if (node == null) return;
        if (node.element % 2 == 1) System.out.print(node.element + " ");
        printOdd(node.left);
        printOdd(node.right);
    }

    public void insert(BinaryNode<AnyType> node) {
        if (root == null) {
            root = node;
            return;
        }
        insertHelper(root, node);
        //balanceTree();
    }

    public void fshi(AnyType x) {
        root = fshiHelper(root, x);
    }

    public void balanceTree() {
        root = balanceTree(root);
    }

    public int nr_leaf(BinaryNode<AnyType> root) {
        if (root == null) {
            return 0;
        }
        if (root.left == null && root.right == null) {
            return 1;
        }
        return nr_leaf(root.left) + nr_leaf(root.right);
    }


    public BinaryNode<AnyType> findParent(BinaryNode<AnyType> node) {
        return findParentHelper(root, node);
    }

    public BinaryNode<AnyType> removeLeaf(BinaryNode<AnyType> current) {

        if (current == null) return null;

        if (current.left == null && current.right == null) return null;

        current.left = removeLeaf(current.left);
        current.right = removeLeaf(current.right);

        return current;
    }

    private void insertHelper(BinaryNode<AnyType> current, BinaryNode<AnyType> newNode) {
        if (newNode.element.compareTo(current.element) < 0) {
            if (current.left == null) {
                current.left = newNode;
            } else insertHelper(current.left, newNode);
        } else {
            if (current.right == null) {
                current.right = newNode;
            } else insertHelper(current.right, newNode);
        }
    }

    private BinaryNode<AnyType> VektortoTree(AnyType[] array) {
        BinaryTree<AnyType> tree = new BinaryTree<AnyType>();
        sort(array);
        invertArray(array);

        for (int i = array.length / 2; i >= 0; i--)
            tree.insert(new BinaryNode<>(array[i]));
        for (int i = 1 + (array.length / 2); i < array.length; i++)
            tree.insert(new BinaryNode<>(array[i]));

        return tree.root;
    }

    private BinaryNode<AnyType> fshiHelper(BinaryNode<AnyType> current, AnyType x) {
        if (current == null) {
            return null;
        }
        if (current.element.compareTo(x) < 0) {
            current.right = fshiHelper(current.right, x);
        } else if (current.element.compareTo(x) > 0) {
            current.left = fshiHelper(current.left, x);
        } else {
            // Rasti 1: Nyja eshte rrenja ose gjethe
            if (current.left == null && current.right == null) {
                return null;
            }
            // Rasti 2: Nyja ka nje femije
            else if (current.left == null) {
                return current.right;
            } else if (current.right == null) {
                return current.left;
            }
            // Rasti 3: Nyja ka dy femije
            else {
                BinaryNode<AnyType> minRight = findMin(current.right);
                current.element = minRight.element;
                current.right = fshiHelper(current.right, minRight.element);
            }
        }
        return current;
    }

    private BinaryNode<AnyType> findParentHelper(BinaryNode<AnyType> Root, BinaryNode<AnyType> child) {
        if (child == null) {
            System.out.println("Femija eshte null");
            return null;
        }
        if (Root == null) {
            System.out.println("Pema eshte boshe");
            return null;
        }
        if (child.element.compareTo(Root.element) > 0) {
            if (Root.right == child) {
                return Root;
            } else {
                return findParentHelper(Root.right, child);
            }
        } else if (child.element.compareTo(Root.element) < 0) {
            if (Root.left == child) {
                return Root;
            } else {
                return findParentHelper(Root.left, child);
            }
        } else {
            return Root;
        }
    }

    private BinaryNode<AnyType> findParentHelper2(BinaryNode<AnyType> root, BinaryNode<AnyType> child) {
        if (child == null) {
            System.out.println("Child is null");
            return null;
        }
        if (root == null) {
            System.out.println("Tree is empty");
            return null;
        }
        if (root.left == child || root.right == child) {
            return root;
        }
        if (child.element.compareTo(root.element) < 0) {
            return findParentHelper(root.left, child);
        } else {
            return findParentHelper(root.right, child);
        }
    }

    private BinaryNode<AnyType> findMin(BinaryNode<AnyType> node) {
        if (node == null) {
            return null;
        } else if (node.left == null) {
            return node;
        } else {
            return findMin(node.left);
        }
    }

    private BinaryNode<AnyType> findMax(BinaryNode<AnyType> node) {
        if (node == null) {
            return null;
        } else if (node.right == null) {
            return node;
        } else {
            return findMax(node.left);
        }
    }

    private int balance(BinaryNode<AnyType> node) {
        return node != null ? (height(node.left) - height(node.right)) : 0;
    }

    private BinaryNode<AnyType> balanceTree(BinaryNode<AnyType> t) {
        if (t == null) return null;

        int balance = balance(t);

        if (balance > 1) {
            if (balance(t.left) < 0) t.left = rotateLeft(t.left);
            t = rotateRight(t);
        } else if (balance < -1) {
            if (balance(t.right) > 0) t.right = rotateRight(t.right);
            t = rotateLeft(t);
        }

        t.left = balanceTree(t.left);
        t.right = balanceTree(t.right);

        return t;
    }

    private BinaryNode<AnyType> rotateRight(BinaryNode<AnyType> t) {
        BinaryNode<AnyType> leftChild = t.left;
        t.left = leftChild.right;
        leftChild.right = t;
        return leftChild;
    }

    private BinaryNode<AnyType> rotateLeft(BinaryNode<AnyType> t) {
        BinaryNode<AnyType> rightChild = t.right;
        t.right = rightChild.left;
        rightChild.left = t;
        return rightChild;
    }


    public void sort(AnyType arr[]) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {

                if (arr[j].compareTo(arr[j + 1]) > 0) {
                    AnyType temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    public void invertArray(AnyType[] array) {

        int start = 0;
        int end = array.length - 1;

        while (start < end) {

            AnyType temp = array[start];
            array[start] = array[end];
            array[end] = temp;

            start++;
            end--;
        }
    }

    public void arrayTree(AnyType V[]) {
        sort(V);
        invertArray(V);
        root = arrayToTree(V, 0, V.length - 1);
    }

    private BinaryNode<AnyType> arrayToTree(AnyType V[], int fillimi, int fundi) {
        if (fillimi > fundi) {
            return null;
        }
        int mesi = (fillimi + fundi) / 2;

        BinaryNode<AnyType> node = new BinaryNode<AnyType>(V[mesi]);

        node.right = arrayToTree(V, fillimi, mesi - 1);

        node.left = arrayToTree(V, mesi + 1, fundi);

        return node;
    }

    public boolean isComplete(BinaryNode<AnyType> root, int index, int numri_nyje) {
        if (root == null) return true;
        if (index >= numri_nyje) return false;
        return isComplete(root.left, 2 * index + 1, numri_nyje) && isComplete(root.right, 2 * index + 2, numri_nyje);
    }

    public boolean isHeap1(BinaryNode<AnyType> root) {
        if (root == null) {
            return true;
        }
        if ((root.left == null || root.element.compareTo(root.left.element) < 0) && ((root.right == null || root.element.compareTo(root.right.element) < 0))) {
            return isHeap1(root.left) && isHeap1(root.right);
        }
        return false;
    }

    public boolean isHeap(BinaryNode<AnyType> node) {
        return isComplete(node, 0, nr_leaf(node)) && (isHeap1(node.left) || isHeap1(node.right));
    }
}



