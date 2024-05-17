package com.example.vitalize.Controlleur;

import com.example.vitalize.Entity.UserSession;
import com.example.vitalize.Entity.Users;
import com.example.vitalize.Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class Login {
    @FXML
    private Button cnxbtn;
    @FXML
    private Button sinscrirebtn;
    @FXML
    private Label emailcc;
    @FXML
    private Label pwdcc;
    @FXML
    private Label cnxcc;
    @FXML
    private TextField email;
    @FXML
    private TextField mdp;
    public void sinscrire(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/vitalize/Registre.fxml"));
        Parent root=loader.load();
        mdp.getScene().setRoot(root);
    }

    public void connection(ActionEvent actionEvent) throws SQLException, IOException {
        int t=0;
        UserService userService=new UserService();
        if (email.getText().isEmpty()){
            t = 1;
            this.emailcc.setText("Vous devez saisir votre email");
        } else {
            this.emailcc.setText("");
        }
        if (mdp.getText().isEmpty()){
            t = 1;
            this.pwdcc.setText("Vous devez saisir votre mot de passe");
        } else {
            this.pwdcc.setText("");
        }
        if(t==0){
            Users user=new Users();
            user.setPassword(mdp.getText());
            user.setEmail(email.getText());
            user=userService.login(user);
            if(user==null)
                this.cnxcc.setText("Desole email ou mot de passe incorrect");
            else if (user.getId()==0) {
                this.cnxcc.setText("Desole Mot de passe incorrect");
            } else if (!user.isStatus()) {
                this.cnxcc.setText("Desole vous etes banni de cette application");
            }
            else {
                UserSession userSession=UserSession.getInstance();
                userSession.setId((long) user.getId());
                userSession.setUser(user);
                if(user.getRole().equals("[\"ROLE_ADMIN\"]")){
                    FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/vitalize/Admin.fxml"));
                    Parent root=loader.load();
                    mdp.getScene().setRoot(root);
                }else {
                    FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/vitalize/Acceuil.fxml"));
                    Parent root=loader.load();
                    mdp.getScene().setRoot(root);
                }
            }
        }
    }

    public void gotoverify(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/vitalize/VerifCode.fxml"));
        Parent root=loader.load();
        mdp.getScene().setRoot(root);
    }
}
