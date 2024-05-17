package com.example.vitalize.Entity;

public class Users {
    private int id;
    private String email;
    private String verif;
    private String role;
    private String password;
    private String nom;
    private String prenom;
    private boolean status;
    private String tel;
    private String avatar;
    private String numcnam;
    private String adresse;
    private String resetToken;

    public Users() {
    }

    public Users(String email, String verif, String role, String password, String nom, String prenom, boolean status, String tel, String avatar, String numcnam, String adresse, String resetToken) {
        this.email = email;
        this.verif = verif;
        this.role = role;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.status = status;
        this.tel = tel;
        this.avatar = avatar;
        this.numcnam = numcnam;
        this.adresse = adresse;
        this.resetToken = resetToken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerif() {
        return verif;
    }

    public void setVerif(String verif) {
        this.verif = verif;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNumcnam() {
        return numcnam;
    }

    public void setNumcnam(String numcnam) {
        this.numcnam = numcnam;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }
}
