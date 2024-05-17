package com.example.vitalize.Entity;

import java.util.Date;

public class Reponse {
    private int id , reclamation_id , patient ;
    private  String message ;
    private Date date ;

    public Reponse() {
    }

    public Reponse(int reclamation_id, int patient, String message, Date date) {
        this.reclamation_id = reclamation_id;
        this.patient = patient;
        this.message = message;
        this.date = date;
    }

    public Reponse(int id, int reclamation_id, int patient, String message, Date date) {
        this.id = id;
        this.reclamation_id = reclamation_id;
        this.patient = patient;
        this.message = message;
        this.date = date;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReclamation_id() {
        return reclamation_id;
    }

    public void setReclamation_id(int reclamation_id) {
        this.reclamation_id = reclamation_id;
    }

    public int getPatient() {
        return patient;
    }

    public void setPatient(int patient) {
        this.patient = patient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Reponse{" +
                "id=" + id +
                ", reclamation_id=" + reclamation_id +
                ", patient=" + patient +
                ", message='" + message + '\'' +
                ", date=" + date +
                '}';
    }
}
