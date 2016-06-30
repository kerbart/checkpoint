package com.kerbart.checkpoint.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class UtilisateurCabinet {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "application_id")
	Integer id;

	@ManyToOne
	Utilisateur utilisateur;

	@ManyToOne	
	Cabinet cabinet;

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

	public Cabinet getCabinet() {
		return cabinet;
	}

	public void setCabinet(Cabinet cabinet) {
		this.cabinet = cabinet;
	}


	
}
