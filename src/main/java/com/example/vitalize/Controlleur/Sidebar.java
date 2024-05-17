package com.example.vitalize.Controlleur;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class Sidebar {
    @FXML
    private Button listeus;
    public void listeutulisateurs(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/vitalize/Admin.fxml"));
        Parent root=loader.load();
        listeus.getScene().setRoot(root);
    }

    public void adduser(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/vitalize/AdduserAdmin.fxml"));
        Parent root=loader.load();
        listeus.getScene().setRoot(root);
    }

    public void NavtoReclamation(ActionEvent actionEvent) throws  IOException{
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/vitalize/ReclamationBOffice.fxml"));
        Parent root=loader.load();
        listeus.getScene().setRoot(root);
    }
}
