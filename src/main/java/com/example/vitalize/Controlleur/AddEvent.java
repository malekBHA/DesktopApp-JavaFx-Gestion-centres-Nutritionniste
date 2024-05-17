package com.example.vitalize.Controlleur;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.example.vitalize.Entity.activite;
import com.example.vitalize.Entity.evenement;
import com.example.vitalize.Service.EventService;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AddEvent implements Initializable {
    @FXML
    private MFXButton Browse;
    @FXML
    private MFXButton close;
    @FXML
    private MFXButton openmap;
    @FXML
    private WebView mapweb;

    @FXML
    private MFXButton add;

    @FXML
    private MFXTextField address;

    @FXML
    private MFXSlider capacite;

    @FXML
    private MFXTextField description;
    @FXML
    private MFXDatePicker Date;

    @FXML
    private MFXTextField image;

    @FXML
    private Label label1;

    @FXML
    private Label label11;

    @FXML
    private MFXTextField name;

    @FXML
    private MFXTextField organiser;
    private WebEngine engine;
    private EventService es = new EventService();

    @FXML
    void Browse(ActionEvent event) {
        Stage primaryStage = new Stage();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a File");

        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
            String fileUrl = selectedFile.toURI().toString();
            image.setText(fileUrl);
        }
    }

    @FXML
    void add(ActionEvent ev) {
        final Pattern namePattern = Pattern.compile("[a-zA-Z]{3,}");

        String imageText = image.getText();
        String t = imageText.replace("%20", " ");
        t = t.replace("/", "\\").replace("file:\\", "");
        String nameText = name.getText();
        String organiserText = organiser.getText();
        String addressText = address.getText();
        String DESCText = description.getText();
        LocalDate eventDate = Date.getValue();
        LocalDate currentDate = LocalDate.now();
        LocalDate minDate = LocalDate.of(currentDate.getYear(), 1, 1);
        LocalDate maxDate = LocalDate.of(currentDate.getYear(), 12, 31);

        final Pattern mapPattern = Pattern.compile("^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$");
        if (!mapPattern.matcher(addressText).matches()) {
            showAlert("Error", "Invalid location format. Please enter coordinates in the format 'latitude, longitude'.");
            return;
        }

        else if (nameText.length() < 3||!namePattern.matcher(nameText).matches()) {
            showAlert("Error", "Name must not be empty and must have at least 3 characters and number free.");
            return;
        }
        else if (organiserText.length() < 3||!namePattern.matcher(organiserText).matches()) {
            showAlert("Error", "Organiser must not be empty and must have at least 3 characters and number free.");
            return;
        }

        else if (DESCText.length() < 3||!namePattern.matcher(DESCText).matches()) {
            showAlert("Error", "Description must not be empty and must have at least 3 characters and number free.");
            return;
        }

        else if ((int) capacite.getValue() == 0) {
            showAlert("Error", "Capacity must be greater than 0.");
            return;
        }

        else if (t.isEmpty()) {
            showAlert("Error", "Please select a photo.");
            return;
        }

        else if (eventDate == null) {
            showAlert("Error", "Please select a date.");
            return;
        }
        else if (eventDate.isBefore(currentDate)) {
            showAlert("Error", "Event date cannot be in the past.");
            return;
        }
        else if (eventDate.isBefore(minDate) || eventDate.isAfter(maxDate)) {
            showAlert("Error", "Event date must be within the current year.");
            return;
        }
       else {
        es.add(new evenement((int) capacite.getValue(), name.getText(), organiser.getText(), address.getText(), description.getText(), t, java.sql.Date.valueOf(Date.getValue())));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/vitalize/DisplayEvent.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene currentScene = ((Node) ev.getSource()).getScene();
        currentScene.setRoot(root);
    }}
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void back(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DisplayEvent.fxml"));
            Parent root = loader.load();

            Scene currentScene = ((Node) actionEvent.getSource()).getScene();

            currentScene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void map(ActionEvent event) {
        engine.load("https://www.google.com/maps/search/Pharmacies/@36.8906336,10.1840512,15z?entry=ttu");
        mapweb.setVisible(true);
        close.setVisible(true);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mapweb.setVisible(false);
        close.setVisible(false);
        engine=mapweb.getEngine();
    }

    public void close(ActionEvent event) {
        mapweb.setVisible(false);
        close.setVisible(false);
    }
}
