package com.example.vitalize.Entity;

import java.util.List;

public class Publication {
    int id,iduser;
    String type,titre,description,image;
    Users user;
    public Publication() {
    }

    public Publication(int iduser, String type, String titre, String description, String image) {
        this.iduser = iduser;
        this.type = type;
        this.titre = titre;
        this.description = description;
        this.image = image;
    }

    public Publication(int id, int iduser, String type, String titre, String description, String image) {
        this.id = id;
        this.iduser = iduser;
        this.type = type;
        this.titre = titre;
        this.description = description;
        this.image = image;
    }
    public int calculateTotalLikes(List<React> reacts) {
        int totalLikes = 0;
        for (React react : reacts) {
            totalLikes += react.getLikeCount();
        }
        return totalLikes;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
