package com.kerbart.checkpoint.controller.dto;

import com.kerbart.checkpoint.model.Ordonnance;

public class OrdonnanceDTO extends CommonDTO {

    Ordonnance ordonnance;
    String patientToken;

    public Ordonnance getOrdonnance() {
        return ordonnance;
    }

    public void setOrdonnance(Ordonnance ordonnance) {
        this.ordonnance = ordonnance;
    }


    public String getPatientToken() {
        return patientToken;
    }

    public void setPatientToken(String patientToken) {
        this.patientToken = patientToken;
    }

}
