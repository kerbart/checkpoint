package com.kerbart.checkpoint.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang3.RandomStringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kerbart.checkpoint.helper.TokenHelper;

@Entity
public class Cabinet {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "application_id")
	Long id;

	@Column
	String name;

	@Column
	String adresse;

	@Column
	String codePostal;

	@Column
	String ville;

	@Column
	String telephone;

	@Column
	String shortCode;

	@Column
	String token;

	@Column
	@JsonIgnore
	String secret;

	@Column
	Boolean current;

	public Cabinet() {
		super();
		this.token = TokenHelper.generateToken();
		this.secret = RandomStringUtils.randomAlphanumeric(16);
		this.shortCode = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public Boolean getCurrent() {
		return current;
	}

	public void setCurrent(Boolean current) {
		this.current = current;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getCodePostal() {
		return codePostal;
	}

	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

}
