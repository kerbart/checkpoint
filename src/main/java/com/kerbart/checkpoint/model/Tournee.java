package com.kerbart.checkpoint.model;

import java.io.Serializable;
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
public class Tournee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tournee_id")
    Long id;

    @Column
    String name;

    @Column
    @Temporal(TemporalType.TIME)
    Date dateCreation;

    @Column
    String token;

    @ManyToOne
    Cabinet cabinet;

    public Tournee() {
        super();
        this.token = TokenHelper.generateToken();
    }

    public Tournee(String name) {
        this();
        this.name = name;
    }

    public Tournee(Cabinet cabinet, String name) {
        this(name);
        this.cabinet = cabinet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Cabinet getCabinet() {
        return cabinet;
    }

    public void setCabinet(Cabinet cabinet) {
        this.cabinet = cabinet;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
