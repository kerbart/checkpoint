package com.kerbart.checkpoint.controller.dto;

import com.kerbart.checkpoint.model.Ordonnance;

public class OrdonnanceDTO {

    Ordonnance ordonnance;
    String applicationToken;
    String utilisateurToken;
    String patientToken;

    public Ordonnance getOrdonnance() {
        return ordonnance;
    }

    public void setOrdonnance(Ordonnance ordonnance) {
        this.ordonnance = ordonnance;
    }

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

    public String getPatientToken() {
        return patientToken;
    }

    public void setPatientToken(String patientToken) {
        this.patientToken = patientToken;
    }

}
