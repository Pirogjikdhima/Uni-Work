package Laboratore.Lab1;

public class QuickSort {
    private int array[];

    public QuickSort(int[] array) {
        this.array = array;
        quicksort(array, 0, array.length - 1);

    }

    private static int partition(int[] a, int low, int high) {
        int pivot = a[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (a[j] <= pivot) {
                i++;

                int temp = a[i];
                a[i] = a[j];
                a[j] = temp;
            }
        }

        int temp = a[i + 1];
        a[i + 1] = a[high];
        a[high] = temp;

        return i + 1;
    }

    private static void quicksort(int[] a, int l, int h) {
        if (l < h) {
            int pivot = partition(a, l, h);
            quicksort(a, l, pivot - 1);
            quicksort(a, pivot + 1, h);
        }
    }

}
