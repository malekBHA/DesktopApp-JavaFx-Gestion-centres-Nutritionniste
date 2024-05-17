package com.example.vitalize.Controlleur;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXSlider;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.example.vitalize.Entity.activite;
import com.example.vitalize.Entity.evenement;
import com.example.vitalize.Service.Activityservice;
import com.example.vitalize.Service.EventService;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class EditActivity implements Initializable {
    @FXML
    private MFXButton Browse;



    @FXML
    private MFXButton Modifier;

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
    private int ID ;
    private Activityservice exp = new Activityservice();
    private EventService es = new EventService();
    private Map<String, Integer> eventMap = new HashMap<>();
    private Stage primaryStage;

    public void setPassedId(int ID) {
        this.ID = ID;
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] t1 = {"Activité brise glace","conférence éducationnelle", "plateau Q/A", "sortie collective", "cercle de discussion"};
        activite e1=new activite();
        for (activite e: exp.rechercheactivite(ID)) {
            e1 = e;
        }

        for(evenement e: es.fetch()) {
            eventMap.put(e.getNom(), e.getId());
            event.getItems().add(e.getNom());
        }
        type.getItems().addAll(t1);

        description.setText(e1.getDescription());
        duree.setValue(e1.getDuree());
        image.setText(e1.getImage());

    }
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
    public void Modifier(javafx.event.ActionEvent actionEvent) {
        String imageText = image.getText();
        String descriptionText = description.getText();
        if (imageText == null || imageText.trim().isEmpty()) {
            showAlert("Error", "Image field is empty");
        }
        if (type.getValue() == null || type.getValue().trim().isEmpty()) {
            showAlert("Error", "type field is empty");
        }
        if (duree.getValue() == 0 ) {
            showAlert("Error", "duree field is empty");
        }
        if (descriptionText == null || descriptionText.trim().isEmpty()) {
            showAlert("Error", "Description field is empty");
        }
        if (eventMap.get(event.getValue()) == null ) {
            showAlert("Error", "Description field is empty");
        }
        else{
        String t=image.getText().replace("%20", " ");

            exp.Edit(ID,type.getValue(),description.getText(),(int)duree.getValue(),t.replace("/", "\\").replace("file:\\",""));
            Stage stage = (Stage) Modifier.getScene().getWindow();
            stage.close();}}
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
