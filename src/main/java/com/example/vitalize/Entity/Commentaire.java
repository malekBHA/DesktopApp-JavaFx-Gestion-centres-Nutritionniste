package com.example.vitalize.Entity;

public class Commentaire {
    int id,id_user,id_pub;
    String contenu;

    public Commentaire() {
    }

    public Commentaire(int id, int id_user, int id_pub, String contenu) {
        this.id = id;
        this.id_user = id_user;
        this.id_pub = id_pub;
        this.contenu = contenu;
    }

    public Commentaire( int id_user, int id_pub, String contenu) {

        this.id_user = id_user;
        this.id_pub = id_pub;
        this.contenu = contenu;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_pub() {
        return id_pub;
    }

    public void setId_pub(int id_pub) {
        this.id_pub = id_pub;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
}
