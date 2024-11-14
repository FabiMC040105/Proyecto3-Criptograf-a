package com.example.textfinder;

import java.io.File;
import java.util.List;

public class QuickSort {

    public static void sort(LinkedListLibrary<File> files) {
        quickSort(files, 0, files.size() - 1);
    }

    private static void quickSort(LinkedListLibrary<File> files, int low, int high) {
        if (low < high) {
            int pi = partition(files, low, high);
            quickSort(files, low, pi - 1);
            quickSort(files, pi + 1, high);
        }
    }

    private static int partition(LinkedListLibrary<File> files, int low, int high) {
        File pivot = files.get(high);
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (files.get(j).getName().compareTo(pivot.getName()) < 0) {
                i++;
                File temp = files.get(i);
                files.set(i, files.get(j));
                files.set(j, temp);
            }
        }
        File temp = files.get(i + 1);
        files.set(i + 1, files.get(high));
        files.set(high, temp);
        return i + 1;
    }


}