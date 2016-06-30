package com.kerbart.checkpoint.controller.dto;

public class PatientSearchDTO {

	String cabinetToken;
	String utilisateurToken;
	String searchTerms;

	public String getCabinetToken() {
		return cabinetToken;
	}

	public void setCabinetToken(String cabinetToken) {
		this.cabinetToken = cabinetToken;
	}

	public String getUtilisateurToken() {
		return utilisateurToken;
	}

	public void setUtilisateurToken(String utilisateurToken) {
		this.utilisateurToken = utilisateurToken;
	}

	public String getSearchTerms() {
		return searchTerms;
	}

	public void setSearchTerms(String searchTerms) {
		this.searchTerms = searchTerms;
	}

}
