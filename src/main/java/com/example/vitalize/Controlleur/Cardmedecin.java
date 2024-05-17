package com.example.vitalize.Controlleur;

import com.example.vitalize.Entity.UserSession;
import com.example.vitalize.Entity.Users;
import com.example.vitalize.Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class  Cardmedecin {
    @FXML
    private Label nom;
    @FXML
    private Label Prenom;
    private Users user;
    public void setdata(Users u){
        nom.setText(u.getNom());
        Prenom.setText(u.getPrenom());
    }

    public void setUser(Users user) {
        this.user = user;
    }
    public void voir(ActionEvent actionEvent) {
        if (user != null && user.getAvatar() != null) {
            String filePath = user.getAvatar();
            File file = new File(filePath);
            if (file.exists()) {
                try {
                    Desktop.getDesktop().open(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void accapter(ActionEvent actionEvent) throws SQLException, IOException {
        boolean status =true;
        int t=0;
        UserService userService=new UserService();
        t=userService.ResponseRequestMedecin(this.user.getId(),status);
        if(t==1){
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/vitalize/Admin.fxml"));
            Parent root=loader.load();
            Prenom.getScene().setRoot(root);
        }
    }

    public void refuser(ActionEvent actionEvent) throws SQLException, IOException {
        boolean status =false;
        int t=0;
        UserService userService=new UserService();
        t=userService.ResponseRequestMedecin(this.user.getId(),status);
        if(t==1){
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/vitalize/Admin.fxml"));
            Parent root=loader.load();
            Prenom.getScene().setRoot(root);
        }
    }
}
