package com.kerbart.checkpoint.controller.dto;

public class CommentaireDTO {

    String commentaire;
    String cabinetToken;
    String utilisateurToken;
    String patientToken;

    public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	

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

    public String getPatientToken() {
        return patientToken;
    }

    public void setPatientToken(String patientToken) {
        this.patientToken = patientToken;
    }

}
