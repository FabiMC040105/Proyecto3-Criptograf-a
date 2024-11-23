package com.example.textfinder;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class FileEncryptor {

    // Genera una clave AES
    public static String generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); // Clave de 128 bits
        SecretKey secretKey = keyGen.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    // Codifica una clave AES desde una cadena Base64
    public static SecretKey decodeKey(String key) {
        byte[] decodedKey = Base64.getDecoder().decode(key);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    // Cifra un texto
    public static byte[] encrypt(String text, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(text.getBytes());
    }

    // Descifra un texto
    public static String decrypt(byte[] encryptedData, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(encryptedData);
        return new String(decryptedBytes);
    }

    // Procesa un archivo (PDF o TXT)
    public static void processFile(File file, String outputFilePath, boolean isEncrypt, String key) throws Exception {
        String content;

        // Determina el tipo de archivo y extrae el texto
        if (file.getName().endsWith(".pdf")) {
            PDFParser parser = new PDFParser(file);
            content = parser.getParsedText();
        } else if (file.getName().endsWith(".txt")) {
            TextFileParser parser = new TextFileParser(file);
            content = parser.getTextContent();
        } else if (file.getName().toLowerCase().endsWith(".docx")) {
            DOCXParser parser = new DOCXParser(file);
            content = parser.parse();
        } else {
            throw new IllegalArgumentException("Formato de archivo no soportado.");
        }

        // Genera o decodifica la clave
        SecretKey secretKey = isEncrypt ? decodeKey(generateKey()) : decodeKey(key);

        // Cifra o descifra el contenido
        byte[] result;
        if (isEncrypt) {
            result = encrypt(content, secretKey);
        } else {
            result = Files.readAllBytes(file.toPath()); // Lee el archivo cifrado
            content = decrypt(result, secretKey);
            result = content.getBytes(); // Reconstruye el texto descifrado
        }

        // Guarda el archivo procesado
        Files.write(Path.of(outputFilePath), result);

        // Imprime la clave si es un cifrado
        if (isEncrypt) {
            System.out.println("Clave de cifrado: " + Base64.getEncoder().encodeToString(secretKey.getEncoded()));
        }
    }
}
