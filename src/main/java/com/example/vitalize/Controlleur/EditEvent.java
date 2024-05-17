package com.example.vitalize.Controlleur;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXSlider;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.example.vitalize.Entity.activite;
import com.example.vitalize.Entity.evenement;
import com.example.vitalize.Service.EventService;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class EditEvent implements Initializable {
    private int ID ;
    @FXML
    private MFXButton close;
    @FXML
    private MFXButton openmap;
    @FXML
    private WebView mapweb;
    private WebEngine engine;


    @FXML
    private MFXButton Browse;

    @FXML
    private MFXDatePicker Date;

    @FXML
    private MFXButton add;

    @FXML
    private MFXTextField address;

    @FXML
    private MFXSlider capacite;

    @FXML
    private MFXTextField description;

    @FXML
    private MFXTextField image;

    @FXML
    private MFXButton Modifier;


    @FXML
    private MFXTextField name;

    @FXML
    private MFXTextField organiser;
    public void setPassedId(int ID) {
        this.ID = ID;
    }
    private Stage primaryStage;
    private EventService es = new EventService();

    public void setPrimaryStage(Stage primaryStage) {



    this.primaryStage = primaryStage;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        evenement e1=new evenement();
        for (evenement e: es.rechercheevenement(ID)) {
            e1 = e;
        }
Date.setText(e1.getDate().toString());
address.setText(e1.getLocalisation());
capacite.setValue(e1.getCapacite());
description.setText(e1.getDescription());
image.setText(e1.getImage());
name.setText(e1.getNom());
organiser.setText(e1.getOrangisateur());
        mapweb.setVisible(false);
        close.setVisible(false);
        engine=mapweb.getEngine();
        engine.load("https://www.google.com/maps?q="+e1.getLocalisation());

    }
    public void Modifier(javafx.event.ActionEvent actionEvent) {

        String imageText = image.getText();
        String t = imageText.replace("%20", " ");
        t = t.replace("/", "\\").replace("file:\\", "");
        String nameText = name.getText().trim();
        String organiserText = organiser.getText().trim();
        String addressText = address.getText().trim();
        final Pattern mapPattern = Pattern.compile("^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$");

        if (nameText.isEmpty() || nameText.length() < 3) {
            showAlert("Error", "Name must not be empty and must have at least 3 characters.");
            return;
        }
        if (organiserText.isEmpty() || organiserText.length() < 3) {
            showAlert("Error", "Organiser must not be empty and must have at least 3 characters.");
            return;
        }
        if (!mapPattern.matcher(addressText).matches()) {
            showAlert("Error", "Invalid location format. Please enter coordinates in the format 'latitude, longitude'.");
            return;
        }
        if (addressText.isEmpty() || addressText.length() < 3) {
            showAlert("Error", "Address must not be empty and must have at least 3 characters.");
            return;
        }

        if ((int) capacite.getValue() == 0) {
            showAlert("Error", "Capacity must be greater than 0.");
            return;
        }

        if (t.isEmpty()) {
            showAlert("Error", "Please select a photo.");
            return;
        }

        LocalDate eventDate = Date.getValue();
        if (eventDate == null) {
            showAlert("Error", "Please select a date.");
            return;
        }
        LocalDate currentDate = LocalDate.now();
        if (eventDate.isBefore(currentDate)) {
            showAlert("Error", "Event date cannot be in the past.");
            return;
        }
        LocalDate minDate = LocalDate.of(currentDate.getYear(), 1, 1);
        LocalDate maxDate = LocalDate.of(currentDate.getYear(), 12, 31);
        if (eventDate.isBefore(minDate) || eventDate.isAfter(maxDate)) {
            showAlert("Error", "Event date must be within the current year.");
            return;
        }

            es.Edit(ID,name.getText(), java.sql.Date.valueOf(Date.getValue()),address.getText(),(int)capacite.getValue(),organiser.getText(),description.getText(),t);
            Stage stage = (Stage) Modifier.getScene().getWindow();
            stage.close();}
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void map(ActionEvent event) {

        mapweb.setVisible(true);
        close.setVisible(true);

    }




    public void close(ActionEvent event) {
        mapweb.setVisible(false);
        close.setVisible(false);
    }
}
