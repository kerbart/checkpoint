package com.kerbart.checkpoint.controller.dto;

public class PatientSearchDTO {

    String applicationToken;
    String utilisateurToken;

    public String getApplicationToken() {
        return applicationToken;
    }

    public void setApplicationToken(String applicationToken) {
        this.applicationToken = applicationToken;
    }

    public String getUtilisateurToken() {
        return utilisateurToken;
    }

    public void setUtilisateurToken(String utilisateurToken) {
        this.utilisateurToken = utilisateurToken;
    }

}
