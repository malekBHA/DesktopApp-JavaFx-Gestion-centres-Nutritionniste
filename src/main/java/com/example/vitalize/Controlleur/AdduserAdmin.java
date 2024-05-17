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
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdduserAdmin implements Initializable {
    @FXML
    private TextField email;
    @FXML
    private TextField mdp;
    @FXML
    private TextField nom;
    @FXML
    private TextField prenom;
    @FXML
    private TextField addres;
    @FXML
    private TextField tel;
    @FXML
    private TextField numcnam;
    @FXML
    private Label emailcc;
    @FXML
    private Label pwdcc;
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
    private Label cnamcc;
    @FXML
    private Label filecc;
    @FXML
    private Label rolecc;
    @FXML
    private ComboBox combobox;
    @FXML
    private VBox box;
    private String imagePath;
    private String role;


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

    public void file(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg","*.jpeg","*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            String selectedFilePath = selectedFile.getPath();
            String formattedFilePath = selectedFilePath.replace("\\", "/");
            this.imagePath =formattedFilePath;

        }
    }

    public void add(ActionEvent actionEvent) throws SQLException {
        int t=0;
        UserService userService=new UserService();
        List<Users> usersList=userService.getAllusers();
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
                for (Users user : usersList) {
                    if (user.getEmail().equalsIgnoreCase(email.getText())) {
                        t = 1;
                        this.emailcc.setText("Email existe");
                        break;
                    }
                }
                if (t == 0) {
                    this.emailcc.setText("");
                }
            }
        }
        if (mdp.getText().isEmpty()){
            t = 1;
            this.pwdcc.setText("Vous devez saisir votre mot de passe");
        } else {
            this.pwdcc.setText("");
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
        if (numcnam.getText().isEmpty() || !numcnam.getText().matches("^\\d+$")) {
            t = 1;
            this.cnamcc.setText("Numero de cnam invalide");
        } else {
            this.cnamcc.setText("");
        }
        if(this.imagePath== null){
            t = 1;
            this.filecc.setText("Image obligatoire");
        }else {
            this.filecc.setText("");
        }
        if(this.role== null){
            t = 1;
            this.rolecc.setText("Role obligatoire");
        }else {
            this.rolecc.setText("");
        }
        if(t==0){
            Users user=new Users();
            user.setEmail(email.getText());
            user.setPassword(mdp.getText());
            user.setNom(nom.getText());
            user.setPrenom(prenom.getText());
            user.setAdresse(addres.getText());
            user.setTel(tel.getText());
            user.setNumcnam(numcnam.getText());
            user.setAvatar(imagePath);
            if(this.role=="Admin"){
                user.setRole("ROLE_ADMIN");
            } else if (this.role=="Patient") {
                user.setRole("ROLE_PATIENT");
            }else {
                user.setRole("ROLE_MEDECIN");
            }
            user.setStatus(true);
            t= userService.registre(user);
            if (t==0){
                this.cnxcc.setText("desole un erreur a interrompu la requête");
            }else {

                this.cnxcc.setText("Ajouté avec succès");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
    }
}
