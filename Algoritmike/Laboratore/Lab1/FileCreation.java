package Laboratore.Lab1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class FileCreation {
    private int numbers[];

    public FileCreation(int n, String path) {

        this.numbers = new int[n];
        Random random = new Random();

        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = random.nextInt(2000);
        }

        try (PrintWriter w = new PrintWriter(path)) {
            for (int i : numbers) {
                w.write(i + " ");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void createFiles(int size) {
        for (int j = 1; size < 1000001; size *= 10, j++) {
            FileCreation fileCreation = new FileCreation(size, "src/Laboratore/Lab1/TextFiles/file" + j + ".txt");
        }
    }

    public static int[] read(String path) {
        List<Integer> numbers = new ArrayList<>();

        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                if (scanner.hasNextInt()) {
                    numbers.add(scanner.nextInt());
                } else {
                    scanner.next();
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
        int[] a = new int[numbers.size()];
        for (int i = 0; i < a.length; i++) {
            a[i] = numbers.get(i);

        }
        return a;
    }
}
