package Laboratore.Lab1;

public class MergeSort {
    private int array[];

    public MergeSort(int[] array) {
        this.array = array;
        mergesort(array, 0, array.length - 1);
    }

    private static void merge(int[] array, int left, int middle, int right) {
        int n1 = middle - left + 1;
        int n2 = right - middle;

        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        System.arraycopy(array, left, leftArray, 0, n1);
        System.arraycopy(array, middle + 1, rightArray, 0, n2);

        int i = 0, j = 0;
        int k = left;

        while (i < n1 && j < n2) {
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            k++;
        }
        while (i < n1) {
            array[k++] = leftArray[i++];
        }
        while (j < n2) {
            array[k++] = rightArray[j++];
        }
    }

    private static void mergesort(int[] array, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;

            mergesort(array, left, middle);
            mergesort(array, middle + 1, right);
            merge(array, left, middle, right);
        }
    }

}
