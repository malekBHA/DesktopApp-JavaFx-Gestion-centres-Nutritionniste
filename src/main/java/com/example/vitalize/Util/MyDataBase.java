package com.example.vitalize.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {
    final String URL = "jdbc:mysql://localhost:3306/vitalize2" ;
    final String USER= "root";
    final String PASSWORD="";
    private Connection connection ;
    private static MyDataBase instance;

    private MyDataBase(){
        try {
            connection = DriverManager.getConnection(URL,USER,PASSWORD) ;
            System.out.println("Connected to Vitalize dataBase !! ");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static MyDataBase getInstance() {
        if (instance == null) {
            instance = new MyDataBase();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
