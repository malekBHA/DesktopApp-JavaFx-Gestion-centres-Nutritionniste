package com.example.vitalize.Controlleur;

import com.example.vitalize.Entity.Users;
import com.example.vitalize.Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.SQLException;

public class Cardutulisateur {
    @FXML
    private Label nom;
    @FXML
    private Label prenom;
    @FXML
    private Label adress;
    @FXML
    private Label tel;
    @FXML
    private Label email;
    @FXML
    private Label status;
    @FXML
    private Label role;
    private  int id;

    public void setId(int id) {

        this.id = id;
    }
    private  Users user;

    public void setUser(Users user) {

        this.user = user;
    }
    public void setdata(Users u){
        nom.setText(u.getNom());
        prenom.setText(u.getPrenom());
        adress.setText(u.getAdresse());
        tel.setText(u.getTel());
        role.setText(u.getRole());
        email.setText(u.getEmail());
        if(u.isStatus()){
            status.setText("Active");
        }else {
            status.setText("Banni");
        }
    }
    public void ban(ActionEvent actionEvent) throws SQLException, IOException {
        UserService userService=new UserService();
        int t;
        t=userService.BanUser(this.id);
        if(t==1){
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/vitalize/Admin.fxml"));
            Parent root=loader.load();
            tel.getScene().setRoot(root);
        }
    }

    public void supprimer(ActionEvent actionEvent) throws SQLException, IOException {
        UserService userService=new UserService();
        int t;
        t=userService.supprimerUser(this.id);
        if(t==1){
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/vitalize/Admin.fxml"));
            Parent root=loader.load();
            tel.getScene().setRoot(root);
        }
    }

    public void modifier(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/vitalize/EditUserAdmin.fxml"));
        Parent root=loader.load();
        EditUserAdmin e=loader.getController();
       e.setUser(this.user);
       e.setData(this.user);
        tel.getScene().setRoot(root);
    }
}
