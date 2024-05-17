package com.example.vitalize.Controlleur;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.example.vitalize.Entity.Publication;
import com.example.vitalize.Service.Servicepublication;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class EditPublication implements Initializable {
    private int ID ;
    private Stage primaryStage;

    @FXML
    private MFXButton Edit;


    @FXML
    private MFXTextField Description;

    @FXML
    private MFXTextField Image;

    @FXML
    private MFXTextField Titre;

    @FXML
    private MFXComboBox<?> Type;
    Servicepublication exp = new Servicepublication();


    public void Edit(ActionEvent event) {
        String t=Image.getText().replace("%20", " ");
        t=t.replace("/", "\\").replace("file:\\", "");
        exp.Edit(ID,Type.getText(),Titre.getText(),Description.getText(),t);
        Stage stage = (Stage) Edit.getScene().getWindow();
        stage.close();
    }

    public void Browse(ActionEvent event) {
        Stage primaryStage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a File");

        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
            String fileUrl = selectedFile.toURI().toString();
            Image.setText(fileUrl);
        }
    }
    public void setPassedId(int ID) {
        this.ID = ID;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Publication e1=new Publication();
        for (Publication e: exp.recherchePublication(ID)) {
            e1 = e;
        }

        String[] t2 = {"Nutrition", "Progrés"};
        Type.setText(e1.getType());
        Titre.setText(e1.getTitre());
        Description.setText(e1.getDescription() );
        Image.setText(e1.getImage());

    }
}
