package com.example.vitalize.Controlleur;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import com.example.vitalize.Entity.Commentaire;
import com.example.vitalize.Service.Servicecommentaire;

import java.net.URL;
import java.util.ResourceBundle;

public class EditCommentaire implements Initializable {
    private int ID;
    private Stage primaryStage;

    @FXML
    private MFXButton editButton;

    @FXML
    private MFXTextField idField;

    @FXML
    private MFXTextField idUserField;

    @FXML
    private MFXTextField idPubField;

    @FXML
    private MFXTextField contenuField;

    Servicecommentaire commentaireService = new Servicecommentaire();

    public void editCommentaire(ActionEvent event) {

        int idUser = Integer.parseInt(idUserField.getText());
        int idPub = Integer.parseInt(idPubField.getText());
        String contenu = contenuField.getText();


        Commentaire commentaire = new Commentaire(ID, idUser, idPub, contenu);


        commentaireService.edit(commentaire);


        Stage stage = (Stage) editButton.getScene().getWindow();
        stage.close();
    }


    public void setPassedId(int ID) {
        this.ID = ID;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Commentaire commentaire = commentaireService.getById(ID);
        idField.setText(String.valueOf(commentaire.getId()));
        idField.setDisable(true);
        idUserField.setText(String.valueOf(commentaire.getId_user()));
        idUserField.setDisable(true);
        idPubField.setText(String.valueOf(commentaire.getId_pub()));
        idPubField.setDisable(true);
        contenuField.setText(commentaire.getContenu());
    }
}
