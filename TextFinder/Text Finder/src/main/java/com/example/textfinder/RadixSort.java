package com.example.textfinder;

import java.io.File;
import java.util.Arrays;

public class RadixSort {

    public static void sort(LinkedListLibrary<File> files) {
        int max = getMax(files);
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSort(files, exp);
        }
    }

    private static int getMax(LinkedListLibrary<File> files) {
        int max = (int) files.get(0).length();
        for (int i = 1; i < files.size(); i++) {
            int size = (int) files.get(i).length();
            if (size > max) {
                max = size;
            }
        }
        return max;
    }

    private static void countingSort(LinkedListLibrary<File> files, int exp) {
        int n = files.size();
        File[] output = new File[n];
        int[] count = new int[10];

        for (int i = 0; i < n; i++) {
            int index = (int) ((files.get(i).length() / exp) % 10);
            count[index]++;
        }

        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }

        for (int i = n - 1; i >= 0; i--) {
            int index = (int) ((files.get(i).length() / exp) % 10);
            output[count[index] - 1] = files.get(i);
            count[index]--;
        }

        for (int i = 0; i < n; i++) {
            files.set(i, output[i]);
        }
    }
}