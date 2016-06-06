package com.kerbart.checkpoint.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Utilisateur {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "utilisateur_id")
	Integer id;

	@Column(unique=true)
	String email;

	@Column
	String password;

	@Column
	String prenom;

	@Column
	String nom;
	
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "user_application", joinColumns = { 
			@JoinColumn(name = "utilisateur_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "application_id", 
					nullable = false, updatable = false) })
	Set<Application> applications;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Set<Application> getApplications() {
		return applications;
	}

	public void setApplications(Set<Application> applications) {
		this.applications = applications;
	}
	
	
}
