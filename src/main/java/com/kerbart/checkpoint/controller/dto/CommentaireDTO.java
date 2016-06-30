package com.kerbart.checkpoint.controller.dto;

public class CommentaireDTO extends CommonDTO {

    String commentaire;
    String patientToken;

    public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}


    public String getPatientToken() {
        return patientToken;
    }

    public void setPatientToken(String patientToken) {
        this.patientToken = patientToken;
    }

}
