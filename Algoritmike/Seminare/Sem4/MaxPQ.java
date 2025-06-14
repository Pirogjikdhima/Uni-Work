package Seminare.Sem4;

public class MaxPQ<Key extends Comparable<Key>> {
    private Key[] pq;
    private int N;
    private int minIndex;

    public MaxPQ( int capacity) {
        pq = (Key[]) new Comparable[capacity + 1];
        N = 0;
        minIndex = -1;
    }

    public MaxPQ() {
        this(1);
    }

    public void insert(Key v) {

        if (N >= pq.length - 1) {
            pq = resize(pq, 2 * pq.length);
        }

        pq[++N] = v;
        swim(N);

        if (minIndex == -1 || v.compareTo(pq[minIndex]) < 0) {
            minIndex = N;
        }
    }

    public Key delMax() {
        if (N == 0) {
            throw new RuntimeException("Priority Queue Underflow");
        }

        Key max = pq[1];
        exch(1, N--);
        pq[N + 1] = null;
        sink(1);

        if (N > 0 && N == (pq.length - 1) / 4) {
            pq = resize(pq, pq.length / 2);
        }

        if (N == 0) {
            minIndex = -1;
        }

        return max;
    }

    public Key max() {
        if (minIndex != -1) {
            return pq[minIndex];
        }
        return null;
    }

    public int size() {
        return N;
    }

    private void swim(int k) {
        while (k > 1 && less(k / 2, k)) {
            exch(k, k / 2);
            k = k / 2;
        }
    }

    private void sink(int k) {
        while (2 * k <= N) {
            int j = 2 * k;
            if (j < N && less(j, j + 1)) {
                j++;
            }
            if (!less(k, j)) {
                break;
            }
            exch(k, j);
            k = j;
        }
    }

    private Key[] resize(Key[] array, int capacity) {
        Key[] temp = (Key[]) new Comparable[capacity];
        for (int i = 1; i <= N; i++) {
            temp[i] = array[i];
        }
        return temp;
    }

    private boolean less(int i, int j) {
        return pq[i].compareTo(pq[j]) < 0;
    }

    private void exch(int i, int j) {
        Key temp = pq[i];
        pq[i] = pq[j];
        pq[j] = temp;
    }

    private boolean isMaxHeap() {
        for (int i = 1; i <= N; i++) {
            if (pq[i] == null) return false;
        }
        for (int i = N + 1; i < pq.length; i++) {
            if (pq[i] != null) return false;
        }
        if (pq[0] != null) return false;
        return isMaxHeapOrdered(1);
    }

    private boolean isMaxHeapOrdered(int k) {
        if (k > N) {
            return true;
        }

        int left = 2 * k;
        int right = 2 * k + 1;

        if (left <= N && less(k, left)) {
            return false;
        }
        if (right <= N && less(k, right)) {
            return false;
        }
        return isMaxHeapOrdered(left) && isMaxHeapOrdered(right);
    }

}