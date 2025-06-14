package Laboratore.Lab1;

import java.io.FileNotFoundException;

public class Lab1 {

    public static void main(String[] args) throws FileNotFoundException {
        FileCreation.createFiles(10);

        for (int iteration = 1; iteration < 7; iteration++) {

            int[] mergeArray = FileCreation.read("src/Laboratore/Lab1/TextFiles/file" + iteration + ".txt");
            long start = System.nanoTime();
            new MergeSort(mergeArray);
            long end = System.nanoTime();
            System.out.println("Iteration: " + iteration + ", MergeSort time " + (end - start) + " ns");

            int[] quickArray = FileCreation.read("src/Laboratore/Lab1/TextFiles/file" + iteration + ".txt");
            start = System.nanoTime();
            new QuickSort(quickArray);
            end = System.nanoTime();
            System.out.println("Iteration: " + iteration + ", QuickSort time " + (end - start) + " ns");
        }
    }
}
