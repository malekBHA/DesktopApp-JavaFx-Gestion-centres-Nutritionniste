package com.example.vitalize.Entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class evenement {
    int id,capacite;
    String nom,orangisateur,description,image,localisation,s;
    List<activite> activites=new ArrayList<>();

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    java.sql.Date date;

    public List<activite> getActivites() {
        return activites;
    }

    public void setActivites(List<activite> activites) {
        this.activites = activites;
    }

    public evenement() {
    }

    public evenement(int capacite, String nom, String orangisateur, String Localisation, String description, String image, java.sql.Date date) {
        this.capacite = capacite;
        this.nom = nom;
        this.orangisateur = orangisateur;
        this.description = description;
        this.image = image;
        this.date = date;
        this.localisation=Localisation;

    }

    public evenement(int id, int capacite, String nom, String orangisateur,String Localisation, String description, String image, java.sql.Date date) {
        this.id = id;
        this.capacite = capacite;
        this.nom = nom;
        this.orangisateur = orangisateur;
        this.description = description;
        this.image = image;
        this.date = date;
        this.localisation=Localisation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String Localisation) {
        this.localisation = Localisation;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getOrangisateur() {
        return orangisateur;
    }

    public void setOrangisateur(String orangisateur) {
        this.orangisateur = orangisateur;
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

    public java.sql.Date getDate() {
        return date;
    }

    public void setDate(java.sql.Date date) {
        this.date = date;
    }

}
