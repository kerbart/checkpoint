package com.kerbart.checkpoint.controller.dto;

/**
 * Ce DTO va contenir les tokens de base (cabinetToken et utilisateurToken)
 * nécéssaires à toutes les transactions
 * 
 * @author damien
 *
 */
public class CommonDTO {

	String cabinetToken;
	String utilisateurToken;

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

}
