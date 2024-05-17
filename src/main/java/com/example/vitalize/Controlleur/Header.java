package com.example.vitalize.Controlleur;

import com.example.vitalize.Entity.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class Header {
    public Button profilebtn;
    public Button publicationbtn;
    public Button commentairebtn;
    public Button decbtn;
    public Button evenementnbtn;
    public Button activitebtn;
    public Button rvdbtn;
    @FXML
    private Button acceuilbtn;
    public void acceuil(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/vitalize/Acceuil.fxml"));
        Parent root=loader.load();
        acceuilbtn.getScene().setRoot(root);
    }

    public void profile(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/vitalize/Profile.fxml"));
        Parent root=loader.load();
        acceuilbtn.getScene().setRoot(root);
    }

   

    public void logout(ActionEvent actionEvent) throws IOException {
        UserSession userSession =UserSession.getInstance();
        userSession.logout();
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/vitalize/Login.fxml"));
        Parent root=loader.load();
        acceuilbtn.getScene().setRoot(root);
    }

    public void Publication(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/vitalize/ShowPublication.fxml"));
        Parent root=loader.load();
        acceuilbtn.getScene().setRoot(root);
    }

    public void Commentaire(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/vitalize/ShowCommentaire.fxml"));
        Parent root=loader.load();
        acceuilbtn.getScene().setRoot(root);
    }
    
    public void Evenement(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/vitalize/DisplayEvent.fxml"));
        Parent root=loader.load();
        acceuilbtn.getScene().setRoot(root);
    }

    public void RendezVous(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/vitalize/RechercheDoctor.fxml"));
        Parent root=loader.load();
        acceuilbtn.getScene().setRoot(root);
    }

    public void Reclamation(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/vitalize/AjoutReclamation.fxml"));
        Parent root=loader.load();
        acceuilbtn.getScene().setRoot(root);
    }
    public void Reponse(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/vitalize/AfficherRecRep.fxml"));
        Parent root=loader.load();
        acceuilbtn.getScene().setRoot(root);
    }


    
}
