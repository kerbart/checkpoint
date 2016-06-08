package com.kerbart.checkpoint.controller.responses;

import com.kerbart.checkpoint.model.Patient;

public class PatientResponse {

    Patient patient;
    ErrorCode error;

    public PatientResponse() {

    }

    public PatientResponse(Patient patient) {
        this();
        this.patient = patient;
    }

    public PatientResponse(ErrorCode error) {
        this();
        this.error = error;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public ErrorCode getError() {
        return error;
    }

    public void setError(ErrorCode error) {
        this.error = error;
    }

}
