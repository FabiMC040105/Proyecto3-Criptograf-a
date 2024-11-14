module com.example.textfinder {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.ooxml;
    requires org.apache.pdfbox;
    requires java.desktop;


    opens com.example.textfinder to javafx.fxml;
    exports com.example.textfinder;
}