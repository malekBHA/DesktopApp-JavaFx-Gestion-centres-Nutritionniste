package com.example.vitalize.Controlleur;

import com.example.vitalize.Entity.UserSession;
import com.example.vitalize.Entity.Users;
import com.example.vitalize.Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Profile implements Initializable {
    @FXML
    private TextField nom;
    @FXML
    private TextField prenom;
    @FXML
    private TextField adress;
    @FXML
    private TextField tel;
    @FXML
    private TextField email;
    @FXML
    private Label nomcc;
    @FXML
    private Label prenomcc;
    @FXML
    private Label adresscc;
    @FXML
    private Label telcc;
    @FXML
    private Label emailcc;
    @FXML
    private Label modifiercc;
    @FXML
    private HBox box;

    public void Modifier(ActionEvent actionEvent) throws SQLException, IOException {
        int t=0;
        UserService userService=new UserService();
        if (email.getText().isEmpty()) {
            t = 1;
            this.emailcc.setText("Email invalide");
        } else {
            String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            Pattern pattern = Pattern.compile(emailPattern);
            Matcher matcher = pattern.matcher(email.getText());
            if (!matcher.matches()) {
                t = 1;
                this.emailcc.setText("Format d'email invalide");
            } else {
                this.emailcc.setText("");
            }
        }
        if (nom.getText().isEmpty()){
            t = 1;
            this.nomcc.setText("Vous devez saisir votre nom");
        } else {
            this.nomcc.setText("");
        }
        if (prenom.getText().isEmpty()){
            t = 1;
            this.prenomcc.setText("Vous devez saisir votre prenom");
        } else {
            this.prenomcc.setText("");
        }
        if (adress.getText().isEmpty()){
            t = 1;
            this.adresscc.setText("Vous devez saisir votre Addresse");
        } else {
            this.adresscc.setText("");
        }
        if (tel.getText().isEmpty() || !tel.getText().matches("^\\d+$")) {
            t = 1;
            this.telcc.setText("Telephone invalide");
        } else if (tel.getText().length()<8) {
            t = 1;
            this.telcc.setText("Le Telephone doit se composer de 8 caracteres");
        } else {
            this.telcc.setText("");
        }
        if(t==0){
            Users user=new Users();
            user.setEmail(email.getText());
            user.setNom(nom.getText());
            user.setPrenom(prenom.getText());
            user.setAdresse(adress.getText());
            user.setTel(tel.getText());
            user=userService.ModifierUser(user,UserSession.getInstance().getUser().getEmail());
            if(user==null){
                this.modifiercc.setText("Modification échoué  probleme d'id");
            }else {
               UserSession userSession =UserSession.getInstance();
               userSession.getUser().setNom(user.getNom());
               userSession.getUser().setPrenom(user.getPrenom());
               userSession.getUser().setTel(user.getTel());
               userSession.getUser().setAdresse(user.getAdresse());
               userSession.getUser().setEmail(user.getEmail());
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/vitalize/Profile.fxml"));
                Parent root=loader.load();
                tel.getScene().setRoot(root);
            }

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        FXMLLoader fxl=new FXMLLoader();
        fxl.setLocation(getClass().getResource("/com/example/vitalize/Header.fxml"));
        Parent root= null;
        try {
            root = fxl.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        box.getChildren().add(root);
        UserSession userSession =UserSession.getInstance();
        Users user=userSession.getUser();
        nom.setText(user.getNom());
        prenom.setText(user.getPrenom());
        adress.setText(user.getAdresse());
        email.setText(user.getEmail());
        tel.setText(user.getTel());



    }
}
