module com.example.vitalize {
    requires javafx.controls;
    requires javafx.fxml;
        requires javafx.web;
            
        requires org.controlsfx.controls;
    requires java.sql;
        requires org.json;
    requires jbcrypt;
    requires MaterialFX;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires java.mail;
    requires org.apache.pdfbox;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires twilio;
    requires itextpdf;

    opens com.example.vitalize.Entity to javafx.base;
    opens com.example.vitalize to javafx.fxml;
    exports com.example.vitalize;
    exports com.example.vitalize.Controlleur;
    opens com.example.vitalize.Controlleur to javafx.fxml;
}