package com.kerbart.checkpoint.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.kerbart.checkpoint.helper.TokenHelper;

@Entity
public class Ordonnance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    Long id;

    @Column
    @Temporal(TemporalType.DATE)
    Date dateDebut;

    @Column
    @Temporal(TemporalType.DATE)
    Date dateFin;

    @ManyToOne
    Patient patient;

    @Column
    String token;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ordonnance", fetch = FetchType.EAGER)
    List<SecuredFile> files;

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

    public List<SecuredFile> getFiles() {
        return files;
    }

    public void setFiles(List<SecuredFile> files) {
        this.files = files;
    }

}