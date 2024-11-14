package com.example.textfinder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TextFileParser {
    private File textFile;
    private String textContent;
    private int wordCounter;

    // Constructor que inicializa el parser con un archivo de texto.
    public TextFileParser(File textFile) {
        this.textFile = textFile;
        this.textContent = parse();  // Llama a parse para extraer el texto del archivo
        this.wordCounter = countWords(this.textContent);  // Cuenta las palabras en el texto extraído
    }

    // Extrae el texto de un archivo de texto.
    public String parse() {
        StringBuilder contentBuilder = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(textFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return contentBuilder.toString();
    }

    // Cuenta el número de palabras en un texto dado.
    int countWords(String text) {
        if (text == null || text.isEmpty()) {
            return 0;  // Si no hay texto, retorna 0
        }
        String[] words = text.split("\\s+");  // Divide el texto en palabras por espacios
        return words.length;  // Retorna la cantidad de palabras
    }

    // Getter para obtener el texto parseado
    public String getTextContent() {
        return textContent;
    }

    // Getter para obtener el contador de palabras
    public int getWordCounter() {
        return wordCounter;
    }
}
