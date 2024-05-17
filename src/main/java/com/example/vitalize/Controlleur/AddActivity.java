package com.example.vitalize.Controlleur;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXSlider;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.example.vitalize.Entity.activite;
import com.example.vitalize.Entity.evenement;
import com.example.vitalize.Service.Activityservice;
import com.example.vitalize.Service.EventService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AddActivity implements Initializable{
    @FXML
    private MFXButton Browse;

    @FXML
    private Pane captchaLabel;
    @FXML
    private Label errorMessage;

    @FXML
    private TextField inputField;

    @FXML
    private Pane captchaLabel1;
    private String captchaValue;

    @FXML
    private MFXButton add;

    @FXML
    private MFXTextField description;

    @FXML
    private MFXSlider duree;

    @FXML
    private MFXTextField image;



    @FXML
    private MFXComboBox<String> type;

    @FXML
    private MFXComboBox<String> event;

    private Activityservice exp = new Activityservice();
    private EventService es = new EventService();
    private Map<String, Integer> eventMap = new HashMap<>();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] t1 = {"Activité brise glace","conférence éducationnelle", "plateau Q/A", "sortie collective", "cercle de discussion"};
        for(evenement e: es.fetch()) {
            eventMap.put(e.getNom(), e.getId());
            event.getItems().add(e.getNom());
        }
        type.getItems().addAll(t1);
        generateCaptcha();
        setCaptcha();


    }
    public void back(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/DisplayActivity.fxml"));
            Parent root = loader.load();

            Scene currentScene = ((Node) actionEvent.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Browse(javafx.event.ActionEvent event) {
        Stage primaryStage = new Stage();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a File");

        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
            String fileUrl = selectedFile.toURI().toString();
            image.setText(fileUrl);
        }
    }

    public void add(javafx.event.ActionEvent ev) {
        String imageText = image.getText();
        String descriptionText = description.getText();
        String input = inputField.getText().trim();


        if (imageText == null || imageText.trim().isEmpty()) {
            showAlert("Error", "Image field is empty");
        }
        else if (type.getValue() == null || type.getValue().trim().isEmpty()) {
            showAlert("Error", "type field is empty");
        }
        else if (duree.getValue() == 0 ) {
            showAlert("Error", "duree field is empty");
        }
        else if (descriptionText == null || descriptionText.trim().isEmpty()) {
            showAlert("Error", "Description field is empty");
        }
        else if (!input.equals(captchaValue.replace(" ",""))) {
            showAlert("Error","The word you entered does not match the generated word.");
        }
        else if (eventMap.get(event.getValue()) == null ) {
            showAlert("Error", "Description field is empty");
        }
        else{
        String t = imageText.replace("%20", " ");
        t = t.replace("/", "\\").replace("file:\\", "");
        int eventId = eventMap.get(event.getValue());
        exp.add(new activite(type.getValue(), eventId, descriptionText, t, (int) duree.getValue()));
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/DisplayActivity.fxml"));
            Parent root = loader.load();
            Scene currentScene = ((Node) ev.getSource()).getScene();
            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load new scene");
        }
        }
        }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void generateCaptcha() {
        StringBuilder valueBuilder = new StringBuilder();
        Random random = new Random();

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        for (int i = 0; i < 6; i++) {
            char charValue = characters.charAt(random.nextInt(characters.length()));
            valueBuilder.append(charValue).append(" ");
        }

        captchaValue = valueBuilder.toString().trim();
    }
    private void setCaptcha() {
        captchaLabel.getChildren().clear();

        double xPos = 0;

        String[] fonts = {"cursive", "sans-serif", "serif", "monospace"};

        for (char charValue : captchaValue.toCharArray()) {
            Text text = new Text(String.valueOf(charValue));
            int rotate = -20 + new Random().nextInt(30);
            int padding = new Random().nextInt(10) + 5;

            String randomColor = String.format("#%06X", new Random().nextInt(0xFFFFFF));

            // Randomly select a font from the 'fonts' array
            String randomFont = fonts[new Random().nextInt(fonts.length)];

            text.setStyle("-fx-rotate: " + rotate + "; -fx-font-family: '" + randomFont + "'; -fx-font-size: 26; -fx-fill: " + randomColor + ";");

            text.setTranslateX(xPos);
            text.setTranslateY(0);

            captchaLabel.getChildren().add(text);

            xPos += text.getBoundsInLocal().getWidth() + padding;
        }
    }
    @FXML
    public void refreshCaptcha() {
        generateCaptcha();
        setCaptcha();
    }

}
