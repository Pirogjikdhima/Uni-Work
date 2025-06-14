package Seminare.Sem2;

public class Seminar2_2 {
//    Ushtrimi 4
    private static int binarySearch(int[] arr, int val, int start, int end) {
        while (start < end) {
            int mid = (start + end) / 2;
            if (arr[mid] < val) {
                start = mid + 1;
            } else {
                end = mid;
            }
        }
        return start;
    }

    // Algoritmi kryesor i Binary Insertion Sort
    public static void binaryInsertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int val = arr[i];

            // Gjen pozicionin e duhur për `val` në nënvektorin e rregulluar
            int j = binarySearch(arr, val, 0, i);

            // Zhvendos elementet nga `j` deri te `i-1` një pozicion djathtas
            for (int k = i; k > j; k--) {
                arr[k] = arr[k - 1];
            }

            // Vendos `val` në pozicionin e duhur
            arr[j] = val;
        }
    }

//    Ushtrimi 5
    public static void DutchFlag(int[] v){
        int start = 0;
        int mid = 0;
        int end = v.length - 1;


        while (mid <= end){
            switch (v[mid]){
                case 0:
                    int temp = v[start];
                    v[start] = v[mid];
                    v[mid] = temp;
                    start++;
                    mid++;
                    break;

                case 1:
                    mid++;
                    break;

                case 2:
                    temp = v[mid];
                    v[mid] = v[end];
                    v[end] = temp;
                    end--;
                    break;
            }
        }
    }


    public static void main(String[] args) {
//        int[] arr = {29, 10, 14, 37, 1,13};
//        binaryInsertionSort(arr);
//
//        System.out.print("Vektori i renditur: ");
//        for (int num : arr) {
//            System.out.print(num + " ");
//        }
        int[] nums = {0, 1, 2, 0, 1, 2, 1, 0};
        DutchFlag(nums);

        System.out.print("Vektori i renditur: ");
        for (int num : nums) {
            System.out.print(num + " ");
        }
    }

}
