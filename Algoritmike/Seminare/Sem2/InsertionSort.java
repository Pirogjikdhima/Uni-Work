package Seminare.Sem2;

public class InsertionSort {
    public static <T extends Comparable<T>> void sort(T[] array) {

        for (int i = 1, j; i < array.length; i++) {
            T key = array[i];
            for (j = i; j > 0 && less(key, array[j - 1]); j--) {
                array[j] = array[j - 1];
            }
            array[j] = key;
        }
    }

    private static <T extends Comparable<T>> boolean less(T v, T w) {
        return v.compareTo(w) < 0;
    }
}
