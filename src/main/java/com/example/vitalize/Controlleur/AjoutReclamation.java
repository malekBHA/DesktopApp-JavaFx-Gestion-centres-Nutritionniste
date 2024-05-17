package com.example.vitalize.Controlleur;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import com.example.vitalize.Entity.Reclamation;
import com.example.vitalize.Entity.UserSession;
import com.example.vitalize.Service.ReclamationService;
import com.example.vitalize.Util.MyDataBase;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AjoutReclamation {

    @FXML
    private TextArea descriptionTF;

    private String selectedImagePath;

    @FXML
    private TextField sujetTF;

    @FXML
    private ComboBox<String> typeComboBox1;

    @FXML
    private ComboBox<String> medecinComboBox;

    private final ReclamationService reclamationService = new ReclamationService();
    private final Map<String, Integer> medecinComboBoxMap = new HashMap<>();
    private final UserSession userSession = UserSession.getInstance();

    @FXML
    public void initialize() {
        typeComboBox1.setItems(FXCollections.observableArrayList(
                "plainte",
                "Suggestion",
                "demande d'information"
        ));
        populateMedecinComboBox();
    }

    private void populateMedecinComboBox() {
        try {
            // Get connection from MaConnexion
            Connection cnx = MyDataBase.getInstance().getConnection();


            // Use prepared statement to populate combo box
            var statement = cnx.prepareStatement("SELECT id, nom FROM users WHERE roles = ?");
            statement.setString(1, "[\"ROLE_MEDECIN\"]");
            var resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                System.out.println("Medecin Name: " + nom); // Debug print statement
                medecinComboBox.getItems().add(nom);
                medecinComboBoxMap.put(nom, id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to populate medecin list.");
        }
    }

    @FXML
    void AjouterReclamation(ActionEvent event) {
        long userId = userSession.getId();
        int unrepliedCount = reclamationService.countUnrepliedReclamations(userId);

        if (unrepliedCount >= 3) {
            showAlert("Error", "You cannot add more than three unreplied reclamations. Please wait for responses to your existing reclamations.");
            return;
        }

        if (isValidReclamation()) {
            String etat = "En Attente";
            String sujet = sujetTF.getText();
            String type = typeComboBox1.getSelectionModel().getSelectedItem();
            String description = descriptionTF.getText();
            String file = selectedImagePath;
            String medecinName = medecinComboBox.getSelectionModel().getSelectedItem();
            int medecinId = medecinComboBoxMap.getOrDefault(medecinName, -1);

            if (medecinId != -1) {
                Date currentDate = new Date();
                int userIdInt = userSession.getId().intValue(); // Convert long to int

                Reclamation reclamation = new Reclamation(sujet, etat, type, description, file, currentDate, userIdInt, medecinId);
                reclamationService.add(reclamation);
                showAlert("Success", "Reclamation added successfully.");
            } else {
                showAlert("Error", "Please select a medecin.");
            }
        } else {
            showAlert("Error", "Please fill in all fields.");
        }
        sujetTF.clear();
        descriptionTF.clear();
        selectedImagePath = null;
    }



    private boolean isValidReclamation() {
        return !sujetTF.getText().isEmpty() &&
                !descriptionTF.getText().isEmpty() &&
                medecinComboBox.getSelectionModel().getSelectedItem() != null;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }





    @FXML
    void AfficheReclamation(ActionEvent event) {
        try {
            sujetTF.getScene().setRoot(FXMLLoader.load(getClass().getResource("/com/example/vitalize/AfficherReclamation.fxml")));
        } catch (IOException e) {
            showAlert("Error", "Failed to load reclamations.");
            e.printStackTrace();
        }
    }

    @FXML
    void selectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                // Define your assets folder path
                String assetsFolderPath = "src/main/resources/com/example/vitalize/assets";


                // Create a File object representing the destination in the assets folder
                File destinationFile = new File(assetsFolderPath + selectedFile.getName());

                // Copy the selected file to the assets folder
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                selectedImagePath = destinationFile.getPath(); // Store the relative path
                // Display the selected image in your UI if needed
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to copy image to assets folder.");
            }
        }
    }

}