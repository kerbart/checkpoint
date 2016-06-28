package com.kerbart.checkpoint.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Commentaire {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	Long id;

	@Column
	String texte;

	@Column
	Date dateCreation;

	@ManyToOne
	Utilisateur createur;
	
	@JsonIgnore
	@ManyToOne
	Patient patient;

	public Commentaire() {
		super();
		this.dateCreation = new Date();
	}

	public Commentaire(Patient patient) {
		this();
		this.patient = patient;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTexte() {
		return texte;
	}

	public void setTexte(String texte) {
		this.texte = texte;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public Utilisateur getCreateur() {
		return createur;
	}

	public void setCreateur(Utilisateur createur) {
		this.createur = createur;
	}
	
	
}
