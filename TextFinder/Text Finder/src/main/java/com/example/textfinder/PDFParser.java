package com.example.textfinder;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.File;
import java.io.IOException;

public class PDFParser {
    private File PDF;
    private String PDFPath;
    private String parsedText;
    private int WordCounter;

    // Constructor de PDFParser que inicializa el parser con un archivo PDF.
    public PDFParser(File PDFFile) {
        this.PDF = PDFFile;
        this.PDFPath = this.PDF.getPath();
        this.parsedText = parse();  // Llama a parse para extraer el texto del documento
        this.WordCounter = countWords(this.parsedText);  // Cuenta las palabras en el texto extraído
    }

    // Extrae el texto de un documento PDF utilizando PDFTextStripper.
    public String parse() {
        if (PDF == null) {
            return null;
        }
        try (PDDocument document = PDDocument.load(PDF)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);  // Devuelve el texto extraído
        } catch (IOException e) {
            e.printStackTrace();  // Imprime la pila de errores si ocurre uno
            return null;
        }
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
    public String getParsedText() {
        return parsedText;
    }

    // Getter para obtener el contador de palabras
    public int getWordCounter() {
        return WordCounter;
    }
}