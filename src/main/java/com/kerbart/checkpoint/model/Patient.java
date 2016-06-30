package com.kerbart.checkpoint.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.kerbart.checkpoint.helper.TokenHelper;

@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    Long id;

    @Column
    String civilite;

    @Column
    String prenom;

    @Column
    String nom;

    @Column
    String adressel1;

    @Column
    String adressel2;

    @Column
    String ville;

    @Column
    String codePostal;

    @Column
    String pays;

    @Column
    String numeroSS;

    @Column
    String telephone;

    @Temporal(TemporalType.DATE)
    Date dateCreation;

    @Column
    Boolean actif;

    @Temporal(TemporalType.DATE)
    @Column
    Date dateNaissance;

    @Column
    String commentaires;

    @ManyToOne(optional = false)
    Cabinet cabinet;

    @Column
    String token;

    public Patient() {
        super();
        this.token = TokenHelper.generateToken();
    }

    public Patient(Cabinet cabinet) {
        this();
        this.cabinet = cabinet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCivilite() {
        return civilite;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdressel1() {
        return adressel1;
    }

    public void setAdressel1(String adressel1) {
        this.adressel1 = adressel1;
    }

    public String getAdressel2() {
        return adressel2;
    }

    public void setAdressel2(String adressel2) {
        this.adressel2 = adressel2;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getNumeroSS() {
        return numeroSS;
    }

    public void setNumeroSS(String numeroSS) {
        this.numeroSS = numeroSS;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Cabinet getCabinet() {
        return cabinet;
    }

    public void setCabinet(Cabinet cabinet) {
        this.cabinet = cabinet;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

}
