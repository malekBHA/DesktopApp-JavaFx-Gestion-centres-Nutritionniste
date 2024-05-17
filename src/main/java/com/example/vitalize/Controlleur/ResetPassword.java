package com.example.vitalize.Controlleur;

import com.example.vitalize.Entity.Users;
import com.example.vitalize.Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class ResetPassword {
    @FXML
    private TextField code;
    @FXML
    private TextField mdp;
    @FXML
    private Label codecc;
    @FXML
    private Label mdpcc;
    @FXML
    private Label cnxcc;
    private String email;

    public void setEmail(String email) {
        this.email = email;
    }
    public void sinscrire(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/vitalize/Registre.fxml"));
        Parent root=loader.load();
        code.getScene().setRoot(root);
    }

    public void connection(ActionEvent actionEvent) throws SQLException, IOException {
        int t=0;
        UserService userService=new UserService();
        if (code.getText().isEmpty()){
            t = 1;
            this.codecc.setText("Vous devez entrer le code de verification");
        } else {
            this.codecc.setText("");
        }
        if (mdp.getText().isEmpty()){
            t = 1;
            this.mdpcc.setText("Vous devez entrer Votre noveaux mdp");
        } else {
            this.mdpcc.setText("");
        }
        if(t==0){
            t=userService.ResetPassword(email,code.getText(),mdp.getText());
            if(t==0){
                this.cnxcc.setText("email introuvalble");
            } else if (t==2) {
                this.codecc.setText("code de verification incorrecte");
            } else if (t==3) {
                this.cnxcc.setText("un erreur inconnu occuru s'il vous plait essayer une autre fois");
            }else {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/vitalize/Login.fxml"));
                Parent root=loader.load();
                code.getScene().setRoot(root);
            }
        }
    }
}
