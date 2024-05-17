package com.example.vitalize.Controlleur;

import com.example.vitalize.Entity.Publication;
import com.example.vitalize.Entity.UserSession;
import com.example.vitalize.Service.EmailService;
import com.example.vitalize.Service.Servicepublication;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddPublication implements Initializable {
    @FXML
    private MFXButton AjoutPub;

    @FXML
    private MFXComboBox<String> Type;

    @FXML
    private MFXTextField Titre;

    @FXML
    private MFXTextField Description;

    @FXML
    private MFXTextField Image;

    private Servicepublication exp = new Servicepublication();

    public void AjoutEx(javafx.event.ActionEvent event) {
        UserSession userSession = UserSession.getInstance();
        if (userSession.getUser() != null) {
            String type = Type.getValue();
            String titre = Titre.getText();
            String description = Description.getText();
            String imageUrl = Image.getText();

            if (isValidInput(type, titre, description, imageUrl)) {
                String t = imageUrl.replace("%20", " ");
                t = t.replace("/", "\\").replace("file:\\", "");
                int userId = userSession.getUser().getId();
                exp.add(new Publication(userId, type, titre, description, t));
                EmailService ems=new EmailService();
                ems.sendEmail("chebaane.mehdi@esprit.tn","New Post Added","Hello, we suggest you take a look at the new post in our feed");
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/ShowPublication.fxml"));
                    Parent root = loader.load();
                    Scene currentScene = ((Node) event.getSource()).getScene();
                    currentScene.setRoot(root);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                showErrorAlert("Please enter valid data for all fields.");
            }
        } else {
            showErrorAlert("User not logged in.");
        }
    }

    public void Browse(javafx.event.ActionEvent event) {
        Stage primaryStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a File");

        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
            String fileUrl = selectedFile.toURI().toString();
            Image.setText(fileUrl);
        }
    }

    private boolean isValidInput(String type, String titre, String description, String imageUrl) {
        return isValidTextField(Titre, titre) &&
                isValidTextField(Description, description) &&
                isValidComboBox(Type, type) &&
                isValidImage(imageUrl);
        // Add more validations if needed
    }

    private boolean isValidTextField(MFXTextField textField, String value) {
        return value != null && !value.isEmpty();
    }

    private boolean isValidComboBox(MFXComboBox<String> comboBox, String value) {
        return value != null && !value.isEmpty();
    }

    private boolean isValidImage(String imageUrl) {
        return imageUrl != null && !imageUrl.isEmpty() && imageUrl.startsWith("file:");
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] t2 = {"Nutrition", "Progrés"};
        Type.getItems().addAll(t2);
    }
}
