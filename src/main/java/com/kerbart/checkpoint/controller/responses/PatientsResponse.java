package com.kerbart.checkpoint.controller.responses;

import java.util.List;

import com.kerbart.checkpoint.model.Patient;

public class PatientsResponse {

    List<Patient> patients;
    ErrorCode error;

    public PatientsResponse() {

    }

    public PatientsResponse(List<Patient> patient) {
        this();
        this.patients = patient;
    }

    public PatientsResponse(ErrorCode error) {
        this();
        this.error = error;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatient(List<Patient> patients) {
        this.patients = patients;
    }

    public ErrorCode getError() {
        return error;
    }

    public void setError(ErrorCode error) {
        this.error = error;
    }

}
