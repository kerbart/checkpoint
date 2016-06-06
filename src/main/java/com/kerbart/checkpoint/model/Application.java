package com.kerbart.checkpoint.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Application {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "application_id")
	Integer id;

	@Column
	String name;

	@Column
	String token;

	@Column
	Boolean parDefaut;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "applications")
	Set<Utilisateur> utilisateurs;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Boolean getParDefaut() {
		return parDefaut;
	}

	public void setParDefaut(Boolean parDefaut) {
		this.parDefaut = parDefaut;
	}

	public Set<Utilisateur> getUtilisateurs() {
		return utilisateurs;
	}

	public void setUtilisateurs(Set<Utilisateur> utilisateurs) {
		this.utilisateurs = utilisateurs;
	}	
	
	
}
