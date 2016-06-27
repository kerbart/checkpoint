package com.kerbart.checkpoint.controller.responses;

import com.kerbart.checkpoint.model.Commentaire;

public class CommentaireResponse {

    Commentaire commentaire;
    ErrorCode error;

    public CommentaireResponse() {

    }

    public CommentaireResponse(Commentaire ordonnance) {
        this();
        this.commentaire = ordonnance;
    }

    public CommentaireResponse(ErrorCode error) {
        this();
        this.error = error;
    }

	public Commentaire getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(Commentaire commentaire) {
		this.commentaire = commentaire;
	}

	public ErrorCode getError() {
		return error;
	}

	public void setError(ErrorCode error) {
		this.error = error;
	}

   
}
