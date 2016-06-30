package com.kerbart.checkpoint.controller.dto;

import com.kerbart.checkpoint.model.Patient;

public class PatientDTO {

    Patient patient;
    String cabinetToken;
    String utilisateurToken;

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
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

}
