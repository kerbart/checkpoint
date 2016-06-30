package com.kerbart.checkpoint.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class UtilisateurNotification {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "utilisateur_id")
	Long id;

	@ManyToOne
	Cabinet cabinet;
	
	@ManyToOne
	Utilisateur utilisateur;
	
	@ManyToOne
	Ordonnance ordonnance;

	@ManyToOne
	Commentaire commentaire;

	@ManyToOne
	Patient patient;

	@Column
	NotificationType type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Cabinet getCabinet() {
		return cabinet;
	}

	public void setCabinet(Cabinet cabinet) {
		this.cabinet = cabinet;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public NotificationType getType() {
		return type;
	}

	public void setType(NotificationType type) {
		this.type = type;
	}

	public Ordonnance getOrdonnance() {
		return ordonnance;
	}

	public void setOrdonnance(Ordonnance ordonnance) {
		this.ordonnance = ordonnance;
	}

	public Commentaire getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(Commentaire commentaire) {
		this.commentaire = commentaire;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

}
