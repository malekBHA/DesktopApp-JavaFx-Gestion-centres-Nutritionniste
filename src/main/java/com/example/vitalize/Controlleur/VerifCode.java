package com.example.vitalize.Controlleur;

import com.example.vitalize.Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class VerifCode {
    @FXML
    private TextField email;
    @FXML
    private Label verifcc;
    @FXML
    private Label cnxcc;


    public void sinscrire(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/vitalize/Registre.fxml"));
        Parent root=loader.load();
        email.getScene().setRoot(root);
    }

    public void connection(ActionEvent actionEvent) throws SQLException, IOException {
        int t=0;
        UserService userService=new UserService();
        if (email.getText().isEmpty()){
            t = 1;
            this.verifcc.setText("Vous devez entrer votre email");
        } else {
            this.verifcc.setText("");
        }
        if(t==0){
            t=userService.SendVerifCode(email.getText());
            if(t==0){
                this.cnxcc.setText("Email introuvable");
            } else if (t==2) {
                this.cnxcc.setText("un erreur inconnu occuru s'il vous plait essayer une autre fois");
            }else{
                FXMLLoader fxll=new FXMLLoader();
                fxll.setLocation(getClass().getResource("/com/example/vitalize/ResetPassword.fxml"));
                Parent roott=fxll.load();
                ResetPassword c=fxll.getController();
                c.setEmail(email.getText());
                email.getScene().setRoot(roott);
            }
        }
    }
}
