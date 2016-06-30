package com.kerbart.checkpoint.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class UtilisateurApplication {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "application_id")
	Integer id;

	@ManyToOne
	Utilisateur utilisateur;

	@ManyToOne	
	Cabinet application;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public Cabinet getApplication() {
		return application;
	}

	public void setApplication(Cabinet application) {
		this.application = application;
	}

	
}
