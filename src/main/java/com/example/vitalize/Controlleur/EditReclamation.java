package com.example.vitalize.Controlleur;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.example.vitalize.Entity.Reclamation;
import com.example.vitalize.Service.ReclamationService;
import com.example.vitalize.Util.MyDataBase;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EditReclamation {

    @FXML
    private TextArea descriptionTF;
    @FXML
    private ComboBox<String> medecinComboBox;
    @FXML
    private ComboBox<String> typeComboBox1;
    @FXML
    private TextField sujetTF;
    @FXML
    private Button fileButton;

    private String filePath;
    private Reclamation reclamation;
    private final ReclamationService rs = new ReclamationService();

    @FXML
    public void initialize() {
        typeComboBox1.setItems(FXCollections.observableArrayList("plainte", "Suggestion", "demande d'information"));
    }


    public void initData(Reclamation reclamation) {
        this.reclamation = reclamation;
        typeComboBox1.setValue(reclamation.getType());
        descriptionTF.setText(reclamation.getDescription());
        sujetTF.setText(reclamation.getSujet());
        filePath = reclamation.getFile(); // Assuming getFile returns a path
        medecinComboBox.setDisable(true);    }

    @FXML
    void chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File");
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            filePath = selectedFile.getAbsolutePath();
            try {
                Files.move(Paths.get(filePath), Paths.get("src/main/resources/com/example/vitalize/assets/" + selectedFile.getName()), StandardCopyOption.REPLACE_EXISTING);
                filePath = "src/main/resources/com/example/vitalize/assets/" + selectedFile.getName(); // Correct path
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void EditReclamation() {
        reclamation.setType(typeComboBox1.getValue());
        reclamation.setDescription(descriptionTF.getText());
        reclamation.setSujet(sujetTF.getText());
        reclamation.setFile(filePath); // Updated to use filePath

        rs.update(reclamation);
        showAlert("Success", "Reclamation updated successfully.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
