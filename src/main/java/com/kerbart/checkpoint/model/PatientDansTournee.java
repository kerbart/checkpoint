package com.kerbart.checkpoint.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class PatientDansTournee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    Long id;

    @ManyToOne
    Patient patient;

    @Column
    Double tarif;

    @Column
    Integer ordre;

    @ManyToOne
    TourneeOccurence tourneeOccurence;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public TourneeOccurence getTourneeOccurence() {
        return tourneeOccurence;
    }

    public void setTourneeOccurence(TourneeOccurence tourneeOccurence) {
        this.tourneeOccurence = tourneeOccurence;
    }

    public Double getTarif() {
        return tarif;
    }

    public void setTarif(Double tarif) {
        this.tarif = tarif;
    }

    public Integer getOrdre() {
        return ordre;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }
}
