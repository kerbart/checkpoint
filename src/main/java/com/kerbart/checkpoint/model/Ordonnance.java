package com.kerbart.checkpoint.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.kerbart.checkpoint.helper.TokenHelper;

public class Ordonnance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    Long id;

    @Column
    Date dateDebut;

    @Column
    Date dateFin;

    @ManyToOne
    Patient patient;

    @Column
    String token;

    @Column
    byte[] photo;

    public Ordonnance() {
        super();
        this.token = TokenHelper.generateToken();
    }

    public Ordonnance(Patient patient) {
        this();
        this.patient = patient;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }
}
