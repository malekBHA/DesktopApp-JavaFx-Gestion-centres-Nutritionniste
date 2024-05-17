package com.example.vitalize.Entity;

import java.io.Serializable;

public class UserSession implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private static UserSession instance;
    private Users user;
    private Publication currentPublication;
    private UserSession() {}

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }
    public Publication getCurrentPublication() {
        return currentPublication;
    }

    public void setCurrentPublication(Publication currentPublication) {
        this.currentPublication = currentPublication;
    }

    public void setUser(Users user) {
        this.user = user;
    }
    public void logout() {
        this.id = null;
        this.user = null;
        this.currentPublication = null;
        this.instance = null;
    }
}
