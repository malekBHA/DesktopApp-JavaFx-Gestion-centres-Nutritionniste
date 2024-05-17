package com.example.vitalize.Entity;

import java.util.Date;

public class Reclamation {
    private String sujet , etat , type , description , file  ;
    private int id , user_id , medecin;
    private Date date ;
    public Reclamation() {
    }
    public Reclamation(String sujet, String type, String description, String file,  Date date, int user_id, int medecin) {
        this.sujet = sujet;
        this.type = type;
        this.description = description;
        this.file = file;
        this.date = date;
        this.user_id = user_id;
        this.medecin = medecin;
    }
    public Reclamation(String sujet, String etat, String type, String description, String file, Date date, int id, int user_id, int medecin) {
        this.sujet = sujet;
        this.etat = etat;
        this.type = type;
        this.description = description;
        this.file = file;
        this.date = date;
        this.id = id;
        this.user_id = user_id;
        this.medecin = medecin;
    }
    public Reclamation( String sujet, String etat, String type, String description, String file, Date date, int user_id, int medecin) {
        this.sujet = sujet;
        this.etat = etat;
        this.type = type;
        this.description = description;
        this.file = file;
        this.date = date;
        this.user_id = user_id;
        this.medecin = medecin;
    }


    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getMedecin() {
        return medecin;
    }

    public void setMedecin(int medecin) {
        this.medecin = medecin;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", sujet='" + sujet + '\'' +
                ", etat='" + etat + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", file='" + file + '\'' +
                ", date=" + date +
                ", user_id=" + user_id +
                ", medecin=" + medecin +
                '}';
    }

}
