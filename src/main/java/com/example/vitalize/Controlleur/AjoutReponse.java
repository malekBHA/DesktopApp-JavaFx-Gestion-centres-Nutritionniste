package com.example.vitalize.Controlleur;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import com.example.vitalize.Entity.Reclamation;
import com.example.vitalize.Entity.Reponse;
import com.example.vitalize.Service.ReclamationService;
import com.example.vitalize.Service.ReponseService;
import com.example.vitalize.Service.EmailService;
import java.io.IOException;
import java.util.Date;

public class AjoutReponse {

    @FXML
    private ComboBox<String> ChangerEtatReclamation;

    @FXML
    private TextArea messageId;

    private final ReponseService rps = new ReponseService();
    private final ReclamationService rs = new ReclamationService();

    private int reclamationId;
    private com.example.vitalize.Service.EmailService emailService = new com.example.vitalize.Service.EmailService();

    public void setReclamationId(int reclamationId) {
        this.reclamationId = reclamationId;
    }

    @FXML
    public void initialize() {
        ObservableList<String> options = FXCollections.observableArrayList(
                "En Attente",
                "En Cours",
                "Resolu"
        );
        ChangerEtatReclamation.setItems(options);
    }

    @FXML
    void AjouterReponse(ActionEvent event) {
        if (isValidReponse()) {
            try {
                String message = messageId.getText();
                Date currentDate = new Date();

                Reponse reponse = new Reponse();
                Reclamation reclamation = rs.getOne(reclamationId); // Retrieve Reclamation by ID

                if (reclamation != null) {
                    reponse.setMessage(message);
                    reponse.setDate(currentDate);
                    reponse.setReclamation_id(reclamationId);
                    reponse.setPatient(1);

                    String selectedEtat = ChangerEtatReclamation.getValue();
                    reclamation.setEtat(selectedEtat); // Update Reclamation etat

                    // Update the etat of Reclamation in the database
                    rs.updateEtat(reclamationId, selectedEtat);

                    // Add response to database
                    rps.add(reponse);

                    // Send email notification
                    String destinataire = "malek.belhadjamor@gmail.com";
                    String sujet = "Réponse à votre réclamation";
                    String contenu = "Vous avez reçu une réponse concernant votre réclamation. Veuillez consulter l'application.";
                    emailService.sendEmail(destinataire, sujet, contenu);

                    showSuccess("Success", "Response added successfully.");
                } else {
                    showAlert("Error", "Reclamation not found for ID: " + reclamationId);
                }
            } catch (Exception e) {
                showAlert("Error", "Failed to add response: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert("Validation Error", "Please fill in all fields.");
        }
    }

    private boolean isValidReponse() {
        return !messageId.getText().isEmpty();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showSuccess(String title, String content) {
        // Create an Alert with the INFORMATION type which is often used for success messages.
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);  // Set the window's title.
        alert.setHeaderText(null);  // Do not use a header text.
        alert.setContentText(content);  // Set the main text to show the success message.

        // Display the alert and wait for it to be dismissed.
        alert.showAndWait();
    }


    public void NavigateToReponses(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/AfficherReponses.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Afficher Reponses");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
