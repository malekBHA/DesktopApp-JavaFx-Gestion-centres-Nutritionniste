package com.example.vitalize.Entity;

public class activite {
    String type_activite,description,image;
    int id,duree,idevent;

    public activite() {
    }

    public activite(String type_activite,int idevent, String description, String image, int duree) {
        this.type_activite = type_activite;
        this.description = description;
        this.image = image;
        this.duree = duree;
        this.idevent = idevent;
    }

    public activite(String type_activite, String description, String image, int id, int duree, int idevent) {
        this.type_activite = type_activite;
        this.description = description;
        this.image = image;
        this.id = id;
        this.duree = duree;
        this.idevent = idevent;
    }

    public String getType_activite() {
        return type_activite;
    }

    public void setType_activite(String type_activite) {
        this.type_activite = type_activite;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public int getIdevent() {
        return idevent;
    }

    public void setIdevent(int idevent) {
        this.idevent = idevent;
    }
}
