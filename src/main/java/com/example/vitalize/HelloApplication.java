package com.example.vitalize;


import com.example.vitalize.Entity.Users;
import com.example.vitalize.Service.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {
        Users user=new Users("admin@admin.com",null,"ROLE_ADMIN","admin","admin","Vitalize",true,"12345678",null,null,null,null);
        UserService userService =new UserService();
        if(userService.admin()==0)
            userService.registre(user);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Vitalize");
        stage.getIcons().add(new Image(getClass().getResource("/com/example/vitalize/img/logo.png").toExternalForm()));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)  {
        launch();
    }
}