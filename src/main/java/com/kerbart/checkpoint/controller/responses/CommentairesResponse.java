package com.kerbart.checkpoint.controller.responses;

import java.util.List;

import com.kerbart.checkpoint.model.Commentaire;

public class CommentairesResponse {

    List<Commentaire> commentaires;
    ErrorCode error;

    public CommentairesResponse() {

    }

  
    public  CommentairesResponse( List<Commentaire> commentaires) {
    	this();
    	this.commentaires = commentaires;
    }
    
    
    public  CommentairesResponse(ErrorCode error) {
    	this.error = error;
    }
    
    
    public List<Commentaire> getCommentaires() {
		return commentaires;
	}



	public void setCommentaires(List<Commentaire> commentaires) {
		this.commentaires = commentaires;
	}



	public ErrorCode getError() {
        return error;
    }

    public void setError(ErrorCode error) {
        this.error = error;
    }

}
