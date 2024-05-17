package com.example.vitalize.Controlleur;

import com.example.vitalize.Entity.UserSession;
import com.example.vitalize.Entity.Users;
import com.example.vitalize.Service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditUserAdmin  {
    @FXML
    private TextField email;
    @FXML
    private TextField nom;
    @FXML
    private TextField prenom;
    @FXML
    private TextField addres;
    @FXML
    private TextField tel;
    @FXML
    private Label emailcc;
    @FXML
    private Label cnxcc;
    @FXML
    private Label nomcc;
    @FXML
    private Label prenomcc;
    @FXML
    private Label addresscc;
    @FXML
    private Label telcc;
    @FXML
    private Label rolecc;
    @FXML
    private ComboBox combobox;
    @FXML
    private VBox box;
    private String role;
    private  Users user;

    public void setUser(Users user) {
        this.user = user;
    }
    public void setData(Users user){
        FXMLLoader fxl=new FXMLLoader();
        fxl.setLocation(getClass().getResource("/com/example/vitalize/sidebar.fxml"));
        Parent root= null;
        try {
            root = fxl.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        box.getChildren().add(root);
        ObservableList<String> list= FXCollections.observableArrayList("Admin","Patient","Medecin");
        combobox.setItems(list);
    nom.setText(user.getNom());
    prenom.setText(user.getPrenom());
    email.setText(user.getEmail());
    addres.setText(user.getAdresse());
    tel.setText(user.getTel());
    if(user.getRole().equals("[\"ROLE_ADMIN\"]")){
        role="Admin";
        combobox.getSelectionModel().selectFirst();
    } else if (user.getRole().equals("[\"ROLE_PATIENT\"]")) {
        role="Patient";
        combobox.getSelectionModel().select(1);
    }else {
        role="Medecin";
        combobox.getSelectionModel().select(2);
    }

    }
    public void deconnection(ActionEvent actionEvent) throws IOException {
        UserSession userSession =UserSession.getInstance();
        userSession.logout();
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/vitalize/Login.fxml"));
        Parent root=loader.load();
        box.getScene().setRoot(root);
    }

    public void select(ActionEvent actionEvent) {
        role=combobox.getSelectionModel().getSelectedItem().toString();
    }


    public void add(ActionEvent actionEvent) throws SQLException {
        int t=0;
        UserService userService=new UserService();
        if (email.getText().isEmpty()) {
            t = 1;
            this.emailcc.setText("Email invalide");
        } else {
            String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
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
        if (addres.getText().isEmpty()){
            t = 1;
            this.addresscc.setText("Vous devez saisir votre Addresse");
        } else {
            this.addresscc.setText("");
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
        if(this.role== null){
            t = 1;
            this.rolecc.setText("Role obligatoire");
        }else {
            this.rolecc.setText("");
        }
        if(t==0){
            String ee=user.getEmail();
            user.setEmail(email.getText());
            user.setNom(nom.getText());
            user.setPrenom(prenom.getText());
            user.setAdresse(addres.getText());
            user.setTel(tel.getText());
            if(this.role=="Admin"){
                user.setRole("ROLE_ADMIN");
            } else if (this.role=="Patient") {
                user.setRole("ROLE_PATIENT");
            }else {
                user.setRole("ROLE_MEDECIN");
            }
            user= userService.ModifierUserAdmin(user,ee);
            if (user==null){
                this.cnxcc.setText("desole un erreur a interrompu la requête");
            }else {

                this.cnxcc.setText("Modifier avec succès");
            }
        }
    }


}
