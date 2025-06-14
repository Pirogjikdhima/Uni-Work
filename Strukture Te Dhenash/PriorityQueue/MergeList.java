package PriorityQueue;

import java.util.Comparator;

class Node {
    int data;
    Node next;

    Node(int key) {
        data = key;
        next = null;
    }
}

// Komparator për të krahasuar vlerat e nyjeve të listës
class NodeComparator implements Comparator<Node> {
    public int compare(Node k1, Node k2) {
        return k1.data - k2.data;
    }
}

public class MergeList {

    // Funksioni mergeList për të bashkuar të gjitha listat
    static Node mergeList(Node[] arr, int K) {
        PriorityQueue<Node> queue = new PriorityQueue<>(new NodeComparator());

        // Shto të gjitha nyjet në rradhë me prioritet
        for (int i = 0; i < K; i++) {
            if (arr[i] != null) {
                queue.add(arr[i]);
            }
        }

        Node dummy = new Node(0);
        Node tail = dummy;

        // Ndërro vlerat nga rradha e prioritetit në listën e bashkuar
        while (!queue.isEmpty()) {
            tail.next = queue.remove();
            tail = tail.next;
            if (tail.next != null) {
                queue.add(tail.next);
            }
        }

        return dummy.next;
    }

    // Funksion për të printuar nyjet e listës
    public static void printList(Node node) {
        while (node != null) {
            System.out.print(node.data + " ");
            node = node.next;
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int N = 4;

        // Një vektor për të ruajtur header të listës
        Node[] a = new Node[N];

        // Linkedlist1
        Node head1 = new Node(1);
        a[0] = head1;
        head1.next = new Node(2);
        head1.next.next = new Node(3);

        // Limkedlist2
        Node head2 = new Node(4);
        a[1] = head2;
        head2.next = new Node(5);

        // Linkedlist3
        Node head3 = new Node(5);
        a[2] = head3;
        head3.next = new Node(6);

        // Linkedlist4
        Node head4 = new Node(7);
        a[3] = head4;
        head4.next = new Node(2);

        Node res = mergeList(a, N);

        printList(res);
    }
}
