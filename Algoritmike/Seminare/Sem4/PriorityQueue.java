package Seminare.Sem4;

import edu.princeton.cs.algs4.MinPQ;

public class PriorityQueue {
    private MaxPQ<Integer> left;
    private MinPQ<Integer> right;
    private int L;
    private int R;

    public PriorityQueue() {
        left = new MaxPQ<Integer>();
        right = new MinPQ<Integer>();
    }

    public double findMedian() {
        int L = left.size();
        int R = right.size();
        if (L == R) return ((double) left.max() + (double) right.min()) / 2;
        else if (L > R) return left.max();
        else return right.min();
    }

    public void insert(int key) {
        double median = findMedian();
        int L = left.size();
        int R = right.size();
        if (key <= median) {
            left.insert(key);
            if (L - R > 1) right.insert(left.delMax());
        } else {
            right.insert(key);
            if (R - L > 1) left.insert(right.delMin());
        }
    }

    public void removeMedian() {
        int L = left.size();
        int R = right.size();
        if (L > R) {
            left.delMax();
        } else {
            right.delMin();
        }
    }

}


