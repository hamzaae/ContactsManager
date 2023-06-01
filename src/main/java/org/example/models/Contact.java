package org.example.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Contact {
    private String id;
    private String nom;
    private String prenom;
    private String tel1;
    private String tel2;
    private String adresse;
    private String email_perso;
    private String email_profess;
    private Genre genre;
    private String managerId;
    private String groupId;



    public enum Genre {
        MAN,
        WOMAN
    }

    public Contact(String nom, String prenom, String tel1, String tel2, String adresse, String email_perso, String email_profess, Genre genre, String managerId, String groupId) {
        this.id = UUID.randomUUID().toString();
        this.nom = nom;
        this.prenom = prenom;
        this.tel1 = tel1;
        this.tel2 = tel2;
        this.adresse = adresse;
        this.email_perso = email_perso;
        this.email_profess = email_profess;
        this.genre = genre;
        this.managerId = managerId;
        this.groupId = groupId;
    }

    public Contact(String id, String nom, String prenom, String tel1, String tel2, String adresse, String email_perso, String email_profess, Genre genre, String managerId, String groupId) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.tel1 = tel1;
        this.tel2 = tel2;
        this.adresse = adresse;
        this.email_perso = email_perso;
        this.email_profess = email_profess;
        this.genre = genre;
        this.managerId = managerId;
        this.groupId = groupId;
    }

    public Contact() {
        this.id = UUID.randomUUID().toString();

    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getId() {
        return id;
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

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmail_perso() {
        return email_perso;
    }

    public void setEmail_perso(String email_perso) {
        this.email_perso = email_perso;
    }

    public String getEmail_profess() {
        return email_profess;
    }

    public void setEmail_profess(String email_profess) {
        this.email_profess = email_profess;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        if (genre.equals("MAN")){
            this.genre = Genre.MAN;
        }
        else {
            this.genre = Genre.WOMAN;
        }
    }

    public String getManagerId(){
        return this.managerId;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", tel1='" + tel1 + '\'' +
                ", tel2='" + tel2 + '\'' +
                ", adresse='" + adresse + '\'' +
                ", email_perso='" + email_perso + '\'' +
                ", email_profess='" + email_profess + '\'' +
                ", genre=" + genre +
                '}';
    }
}
