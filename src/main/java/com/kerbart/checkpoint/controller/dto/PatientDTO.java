package com.kerbart.checkpoint.controller.dto;

import com.kerbart.checkpoint.model.Patient;

public class PatientDTO extends CommonDTO {

    Patient patient;

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
