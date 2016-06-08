package com.kerbart.checkpoint.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class TourneeOccurence implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tournee_id")
    Long id;

    @ManyToOne
    Tournee tournee;

    @OneToMany(mappedBy = "tourneeOccurence")
    Set<PatientDansTournee> patients;

    @Column
    @Temporal(TemporalType.DATE)
    Date dateDebut;

    @Column
    @Temporal(TemporalType.DATE)
    Date dateFin;

    @Column
    Double total;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tournee getTournee() {
        return tournee;
    }

    public void setTournee(Tournee tournee) {
        this.tournee = tournee;
    }

    public Set<PatientDansTournee> getPatients() {
        return patients;
    }

    public void setPatients(Set<PatientDansTournee> patients) {
        this.patients = patients;
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

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
