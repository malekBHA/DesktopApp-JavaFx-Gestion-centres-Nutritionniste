package com.example.vitalize.Controlleur;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import com.example.vitalize.Entity.Reponse;
import com.example.vitalize.Service.ReponseService;

public class EditReponse {

    @FXML
    private TextField messageTF;

    private Reponse reponse;
    private ReponseService reponseService = new ReponseService();

    public void initData(Reponse reponse) {
        this.reponse = reponse;
        messageTF.setText(reponse.getMessage());
    }
    @FXML
    void editReponse() {
        if (reponse != null) {
            try {
                reponse.setMessage(messageTF.getText());
                reponseService.update(reponse);
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Update Successful");
                successAlert.setHeaderText(null);
                successAlert.setContentText("The response has been successfully updated.");
                successAlert.showAndWait();
            } catch (Exception e) {
                Alert failureAlert = new Alert(Alert.AlertType.ERROR);
                failureAlert.setTitle("Update Failed");
                failureAlert.setHeaderText(null);
                failureAlert.setContentText("Failed to update the response. Please try again.");
                failureAlert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Response Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a response to edit.");
            alert.showAndWait();
        }
    }

}
