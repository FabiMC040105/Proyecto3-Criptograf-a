package com.example.textfinder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.awt.datatransfer.Clipboard;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.io.BufferedReader;
import java.io.FileReader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.awt.Desktop;
import javafx.event.ActionEvent;        // Para manejar eventos de acción como clics en botones
import javafx.scene.Scene;            // Para manejar las escenas de la ventana
import javafx.scene.control.Button;    // Para usar botones en la interfaz
import javafx.scene.control.Label;     // Para mostrar etiquetas (como mensajes)
import javafx.scene.control.TextField; // Para el campo de texto donde el usuario ingresa la clave
import javafx.scene.layout.VBox;       // Para organizar los elementos en un contenedor vertical
import javafx.stage.Modality;          // Para definir la modalidad de la ventana emergente
import javafx.stage.Stage;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.crypto.SecretKey;
import java.awt.*;
import java.awt.datatransfer.StringSelection;


public class Interface_Controller implements Initializable {

    public MenuButton btn_Sort;
    Indizador indizarFile =  new Indizador();

    LinkedListLibrary<File> lista_archivos = new LinkedListLibrary<>();

    LinkedListLibrary<File> filesresults = new LinkedListLibrary<>();

    FileChooser.ExtensionFilter txt = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
    FileChooser.ExtensionFilter pdf = new FileChooser.ExtensionFilter("PDF Files (*.pdf)", "*.pdf");
    FileChooser.ExtensionFilter docx = new FileChooser.ExtensionFilter("Word Documents (*.docx)", "*.docx");
    FileChooser.ExtensionFilter encrypted = new FileChooser.ExtensionFilter("Encrypted File (*.encrypted)", "*.encrypted");

    @FXML
    private ListView<String> listview_biblioteca;

    @FXML
    private ListView<String> listview_results;

    @FXML
    private TextFlow textFlow;

    private Indizador indizador = new Indizador();
    private String textoParseado = "";

    @FXML
    private TextField TextFieldBuscar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listview_biblioteca.setItems(FXCollections.observableArrayList());
        listview_results.setItems(FXCollections.observableArrayList());

        // Añade un listener para el ListView de resultados
        listview_results.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Obtener el índice del elemento seleccionado
                int index = listview_results.getSelectionModel().getSelectedIndex();
                // Verificar si el índice es válido
                if (index >= 0) {
                    // Obtener el nombre del archivo seleccionado como cadena
                    // Obtener el archivo correspondiente al nombre
                    File archivoSeleccionado = lista_archivos.get(newValue);
                    // Mostrar el contenido del archivo seleccionado
                    mostrarContenidoArchivo(archivoSeleccionado);
                }
            }
        });
    }



    public void btn_anadir_doc(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(txt, pdf, docx, encrypted); //añade los filtros para escoger archivos
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null){
            lista_archivos.add(selectedFile);
            listview_biblioteca.getItems().add(selectedFile.getName());
        }
    }

    public void btn_actualizar_doc(ActionEvent actionEvent) {
        // Obtener el índice del elemento seleccionado
        int indice = listview_biblioteca.getSelectionModel().getSelectedIndex();
        String nombreFile = listview_biblioteca.getSelectionModel().getSelectedItem();
        System.out.println("Documento eliminado: " + nombreFile);
        // Si se ha seleccionado un elemento, actualizarlo
        if (indice >= 0) {
            // Eliminar el archivo de la lista
            File archivoSeleccionado = lista_archivos.get(nombreFile);
            lista_archivos.remove(archivoSeleccionado);
            // Conseguir el path del archivo
            String path = archivoSeleccionado.getAbsolutePath();
            File file_actualizado = new File(path);
            lista_archivos.add(file_actualizado);

        }
    }

    // DESENCRIPTAR ARCHIVO
    public void btn_desencript_doc(ActionEvent actionEvent) {
        // Obtener el índice del elemento seleccionado
        int selectedIndex = listview_biblioteca.getSelectionModel().getSelectedIndex();
        String nombreFile = listview_biblioteca.getSelectionModel().getSelectedItem();

        if (selectedIndex >= 0) {
            File archivoSeleccionado = lista_archivos.get(nombreFile);

            // Crear ventana emergente para ingresar clave
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Clave de desencriptación");

            Label label = new Label("Ingrese la clave para desencriptar:");
            TextField claveField = new TextField();
            Button btnEjecutar = new Button("Desencriptar");

            btnEjecutar.setOnAction(event -> {
                String claveIngresada = claveField.getText();

                try {
                    // Decodificar clave
                    SecretKey secretKey = FileEncryptor.decodeKey(claveIngresada);

                    // Leer contenido cifrado
                    byte[] contenidoCifrado = Files.readAllBytes(archivoSeleccionado.toPath());

                    // Descifrar contenido binario
                    byte[] contenidoDescifrado = FileEncryptor.decryptBytes(contenidoCifrado, secretKey);

                    // Guardar el archivo descifrado
                    String nuevoNombre = archivoSeleccionado.getName().replace(".encrypted", "_descifrado.txt");
                    File archivoDescifrado = new File(archivoSeleccionado.getParent(), nuevoNombre);
                    Files.write(archivoDescifrado.toPath(), contenidoDescifrado);

                    // Mostrar mensaje de éxito
                    mostrarInformacion("Desencriptación completada", "Archivo desencriptado correctamente.");
                    dialog.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    mostrarError("Error al desencriptar", e.getMessage());
                }
            });

            VBox layout = new VBox(10);
            layout.getChildren().addAll(label, claveField, btnEjecutar);
            Scene scene = new Scene(layout, 300, 150);
            dialog.setScene(scene);
            dialog.showAndWait();

        } else {
            mostrarAdvertencia("Advertencia", "Ningún archivo seleccionado", "Seleccione un archivo para continuar.");
        }
    }



    // ENCRIPTAR ARCHIVO
    public void btn_encript_doc(ActionEvent actionEvent) {
        // Obtener el índice del elemento seleccionado
        int selectedIndex = listview_biblioteca.getSelectionModel().getSelectedIndex();
        String nombreFile = listview_biblioteca.getSelectionModel().getSelectedItem();

        if (selectedIndex >= 0) {
            File archivoSeleccionado = lista_archivos.get(nombreFile);

            try {
                // Generar clave AES
                String claveAES = FileEncryptor.generateKey();
                SecretKey secretKey = FileEncryptor.decodeKey(claveAES);

                // Determinar el tipo de archivo y leer su contenido
                String extension = archivoSeleccionado.getName().substring(archivoSeleccionado.getName().lastIndexOf(".") + 1);
                String contenido;
                if (extension.equalsIgnoreCase("pdf")) {
                    PDFParser parser = new PDFParser(archivoSeleccionado);
                    contenido = parser.getParsedText();
                } else if (extension.equalsIgnoreCase("txt")) {
                    TextFileParser parser = new TextFileParser(archivoSeleccionado);
                    contenido = parser.getTextContent();
                }else if (extension.equalsIgnoreCase("docx")) {
                    DOCXParser parser = new DOCXParser(archivoSeleccionado);
                    contenido = parser.parse();
                } else {
                    throw new IllegalArgumentException("Formato de archivo no soportado.");
                }

                // Cifrar el contenido
                byte[] contenidoCifrado = FileEncryptor.encrypt(contenido, secretKey);

                // Guardar el archivo cifrado
                File archivoCifrado = new File(archivoSeleccionado.getParent(), archivoSeleccionado.getName() + ".encrypted");
                Files.write(archivoCifrado.toPath(), contenidoCifrado);
                // cpoiar la clave en el porta papeles
                copiarPortaPapeles(claveAES);

                // Mostrar clave al usuario
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Encriptación completada");
                alert.setHeaderText("Archivo encriptado correctamente");
                alert.setContentText("Clave de cifrado (Copiada en portapapeles):\n" + claveAES);
                alert.showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
                mostrarError("Error al encriptar", e.getMessage());
            }
        } else {
            mostrarAdvertencia("Advertencia", "Ningún archivo seleccionado", "Seleccione un archivo para continuar.");
        }
    }



    public void btn_IndizarArchivos(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Archivos indizados correctamente");
        for (File archivo : lista_archivos) {
            indizarFile.indizacion(archivo);
        }
        alert.showAndWait();
    }

    public void btn_Buscar_Archivos(ActionEvent actionEvent) {
        String palabraBuscar = TextFieldBuscar.getText().trim();
        listview_results.getItems().clear();
        for (File archivo : lista_archivos) {
            if (buscarPalabraEnArchivo(palabraBuscar, archivo)) {
                listview_results.getItems().add(archivo.getName());
                filesresults.add(archivo);
            }
        }
        filesresults.printList();
    }

    // Este método se llama cuando se presiona el botón "Abrir Documento"
    public void btn_Abrir_Doc1(ActionEvent actionEvent) {
        int indice = listview_results.getSelectionModel().getSelectedIndex();
        String nombreFile = listview_results.getSelectionModel().getSelectedItem();
        // Si se ha seleccionado un elemento, actualizarlo
        if (indice >= 0) {
            File archivoSeleccionado = filesresults.get(nombreFile);
            mostrarContenidoArchivo(archivoSeleccionado);
            // Intentar abrir el archivo con la aplicación predeterminada
            try {
                Desktop.getDesktop().open(archivoSeleccionado);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No se ha seleccionado ningún archivo.");
        }
    }

    private boolean buscarPalabraEnArchivo(String palabra, File archivo) {
        String nombreArchivo = archivo.getName();
        String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf(".") + 1);

        if (extension.equalsIgnoreCase("docx")) {
            try (FileInputStream fis = new FileInputStream(archivo)) {
                XWPFDocument document = new XWPFDocument(fis);
                List<XWPFParagraph> paragraphs = document.getParagraphs();

                for (XWPFParagraph paragraph : paragraphs) {
                    String texto = paragraph.getText();
                    if (texto != null && texto.contains(palabra)) {
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (extension.equalsIgnoreCase("txt")) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    if (linea.contains(palabra)) {
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (extension.equalsIgnoreCase("pdf")) {
            try (PDDocument document = PDDocument.load(archivo)) {
                PDFTextStripper pdfStripper = new PDFTextStripper();
                String texto = pdfStripper.getText(document);
                if (texto != null && texto.contains(palabra)) {
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }


    private void mostrarInformacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait(); // Muestra el cuadro de diálogo hasta que el usuario lo cierre
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);         // Título de la ventana de error
        alert.setHeaderText(null);
        alert.setContentText(mensaje); // Contenido del mensaje de error
        alert.showAndWait();            // Muestra el cuadro de diálogo hasta que el usuario lo cierre
    }

    private void mostrarAdvertencia(String titulo, String encabezado, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    private void copiarPortaPapeles(String texto){
        // Crear un objeto StringSelection con el texto a copiar
        StringSelection stringSelection = new StringSelection(texto);

        // Obtener el portapapeles del sistema
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        // Establecer el texto en el portapapeles
        ((Clipboard) clipboard).setContents(stringSelection, null);
        System.out.println("Texto copiado al portapapeles: " + texto);
    }




    private void mostrarContenidoArchivo(File archivo) {
        // Verificar si el archivo existe
        if (!archivo.exists()) {
            System.out.println("El archivo no existe: " + archivo.getAbsolutePath());
            return;
        }

        // Obtener el nombre del archivo sin la ruta completa
        String nombreArchivo = archivo.getName();
        String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf(".") + 1);

        StringBuilder contenido = new StringBuilder();

        try {
            if (extension.equalsIgnoreCase("docx")) {
                XWPFDocument document = new XWPFDocument(new FileInputStream(archivo));
                List<XWPFParagraph> paragraphs = document.getParagraphs();

                for (XWPFParagraph paragraph : paragraphs) {
                    contenido.append(paragraph.getText()).append("\n");
                }
            } else if (extension.equalsIgnoreCase("txt")) {
                try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                    String linea;
                    while ((linea = br.readLine()) != null) {
                        contenido.append(linea).append("\n");
                    }
                }
            } else if (extension.equalsIgnoreCase("pdf")) {
                try (PDDocument document = PDDocument.load(archivo)) {
                    PDFTextStripper pdfStripper = new PDFTextStripper();
                    contenido.append(pdfStripper.getText(document));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Mostrar el contenido del archivo
        resaltarPalabra(contenido.toString(), TextFieldBuscar.getText().trim());
    }

    private void resaltarPalabra(String contenido, String palabraBuscar) {
        textFlow.getChildren().clear();
        if (palabraBuscar.isEmpty()) {
            textFlow.getChildren().add(new Text(contenido));
            return;
        }

        int index = 0;
        while (index < contenido.length()) {
            int wordIndex = contenido.indexOf(palabraBuscar, index);
            if (wordIndex == -1) {
                textFlow.getChildren().add(new Text(contenido.substring(index)));
                break;
            }
            if (wordIndex > index) {
                textFlow.getChildren().add(new Text(contenido.substring(index, wordIndex)));
            }
            Text highlightedText = new Text(contenido.substring(wordIndex, wordIndex + palabraBuscar.length()));
            highlightedText.setStyle("-fx-fill: black; -fx-font-weight: bold; -fx-underline: true; -fx-background-color: red;");
            textFlow.getChildren().add(highlightedText);
            index = wordIndex + palabraBuscar.length();
        }
    }


    public void Sort_by_Name(ActionEvent actionEvent) {
        System.out.println("sort by name/QuickSort");
        QuickSort.sort(filesresults);
        actualizarListViewResults();

    }

    private void actualizarListViewResults() {
        listview_results.getItems().clear();
        for (File file : filesresults) {
            listview_results.getItems().add(file.getName());
        }
    }

    public void Sort_by_Date(ActionEvent actionEvent) {
        System.out.println("sort by date/BubbleSort");
        BubbleSort.sort(filesresults);
        actualizarListViewResults();
    }

    public void Sort_by_size(ActionEvent actionEvent) {
        System.out.println("sort by size/RadixSort");
        RadixSort.sort(filesresults);
        actualizarListViewResults();
    }

}

