package com.kerbart.checkpoint.controller.responses;

import com.kerbart.checkpoint.model.Utilisateur;

public class UtilisateurResponse {

    Utilisateur utilisateur;
    ErrorCode error;

    public UtilisateurResponse() {

    }

    public UtilisateurResponse(Utilisateur utilisateur) {
        this();
        this.utilisateur = utilisateur;
    }

    public UtilisateurResponse(ErrorCode error) {
        this();
        this.error = error;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public ErrorCode getError() {
        return error;
    }

    public void setError(ErrorCode error) {
        this.error = error;
    }

}
