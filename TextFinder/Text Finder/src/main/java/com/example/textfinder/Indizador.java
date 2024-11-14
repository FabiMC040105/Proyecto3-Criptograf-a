package com.example.textfinder;

import java.io.File;

public class Indizador {

    private AVLTree avlTree;

    // Constructor
    public Indizador() {
        avlTree = new AVLTree();
    }

    public void indizacion(File file) {
        String texto = "";
        if (file.getName().toLowerCase().endsWith(".docx")) {
            DOCXParser docxParser = new DOCXParser(file);
            texto = docxParser.parse();

        } else if (file.getName().toLowerCase().endsWith(".pdf")) {
            PDFParser pdfParser = new PDFParser(file);
            texto = pdfParser.parse();
        } else if (file.getName().toLowerCase().endsWith(".txt")) {
            TextFileParser textFileParser = new TextFileParser(file);
            texto = textFileParser.parse();
        } else {
            System.out.println("No se puede parsear este documento");
            return;
        }
        // Insertar texto parseado en el árbol AVL
        String[] palabras = texto.split("\\s+");
        int posicion = 1; // Inicializar el número de palabra en 1
        for (String palabra : palabras) {
            avlTree.insert(palabra, posicion, file.getName());
            posicion ++;
        }
        System.out.println("Documento indizado: " + file.getName());
        avlTree.inOrder();
    }

}
