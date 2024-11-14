package com.example.textfinder;

import java.io.File;

public class BubbleSort {

    public static void sort(LinkedListLibrary<File> files) {
        int n = files.size();
        boolean swapped;
        do {
            swapped = false;
            for (int i = 0; i < n - 1; i++) {
                if (files.get(i).lastModified() > files.get(i + 1).lastModified()) {
                    swap(files, i, i + 1);
                    swapped = true;
                }
            }
            n--; // Reduce el rango de comparación después de cada pasada
        } while (swapped);
    }

    private static void swap(LinkedListLibrary<File> files, int i, int j) {
        File temp = files.get(i);
        files.set(i, files.get(j));
        files.set(j, temp);
    }
}
