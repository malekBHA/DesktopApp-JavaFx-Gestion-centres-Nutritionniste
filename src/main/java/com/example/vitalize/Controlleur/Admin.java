package com.example.vitalize.Controlleur;

import com.example.vitalize.Entity.UserSession;
import com.example.vitalize.Entity.Users;
import com.example.vitalize.Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class Admin implements Initializable {
    @FXML
    private VBox box;
    @FXML
    private VBox vbox;
    @FXML
    private VBox boxnotif;
    public void deconnection(ActionEvent actionEvent) throws IOException {
        UserSession userSession =UserSession.getInstance();
        userSession.logout();
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/example/vitalize/Login.fxml"));
        Parent root=loader.load();
        vbox.getScene().setRoot(root);
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
        List<Users> users;
        UserService userService=new UserService();
        try {
            users=userService.getAllusers();
            for(int i=0;i<users.size();i++){
                FXMLLoader fxll=new FXMLLoader();
                fxll.setLocation(getClass().getResource("/com/example/vitalize/cardutulisateur.fxml"));
                Parent roott=fxll.load();
                Cardutulisateur c=fxll.getController();
                c.setdata(users.get(i));
                c.setUser(users.get(i));
                c.setId(users.get(i).getId());
                vbox.getChildren().add(roott);}
            List<Users> users2=userService.getAllMedecinRequest();
            for(int i=0;i<users2.size();i++){
                FXMLLoader fxll2=new FXMLLoader();
                fxll2.setLocation(getClass().getResource("/com/example/vitalize/cardmedecin.fxml"));
                Parent roott2=fxll2.load();
                Cardmedecin c2=fxll2.getController();
                c2.setdata(users2.get(i));
                c2.setUser(users2.get(i));
                boxnotif.getChildren().add(roott2);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
