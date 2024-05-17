package com.example.vitalize.Controlleur;

import com.example.vitalize.Entity.Commentaire;
import com.example.vitalize.Entity.UserSession;
import com.example.vitalize.Service.Servicecommentaire;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class AddCommentaire implements Initializable {

    @FXML
    private MFXButton AjoutComment;

    @FXML
    private MFXTextField id_pub;

    @FXML
    private MFXTextField Contenu;

    private static final List<String> badWords = Arrays.asList("badword1", "badword2", "badword3");
    private final Servicecommentaire serviceCommentaire = new Servicecommentaire();

    public AddCommentaire() {

    }

    private String filterBadWords(String content) {
        for (String badWord : badWords) {
            content = content.replaceAll("(?i)" + badWord, "*".repeat(badWord.length())); // Replace bad words with asterisks
        }
        return content;
    }

    public void populateFields(int publicationId) {
        id_pub.setText(String.valueOf(publicationId));
        id_pub.setDisable(true);
    }

    @FXML
    public void AjoutComment(ActionEvent event) {
        try {
            UserSession userSession = UserSession.getInstance();
            if (userSession.getUser() != null) {
                String content = filterBadWords(Contenu.getText());
                int userId = userSession.getUser().getId();
                String pubIdText = id_pub.getText();

                if (isValidInput(content) && isValidPubId(pubIdText)) {
                    int userID = userId;
                    int pubID = Integer.parseInt(pubIdText);

                    Commentaire commentaire = new Commentaire(1, userID, pubID, content);
                    serviceCommentaire.add(commentaire);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/ShowPublication.fxml"));
                    Parent root = loader.load();

                    Scene currentScene = ((Node) event.getSource()).getScene();
                    currentScene.setRoot(root);
                } else {
                    showErrorAlert("Please enter valid content for the comment.");
                }
            } else {
                showErrorAlert("User not logged in.");
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();

        }
    }


    private boolean isValidPubId(String pubIdText) {
        return pubIdText != null && !pubIdText.isEmpty() && pubIdText.matches("\\d+");
    }

    private boolean isValidInput(String content) {
        return content != null && !content.isEmpty();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
