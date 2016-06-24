package com.kerbart.checkpoint.controller.responses;

import java.util.List;

import com.kerbart.checkpoint.model.Ordonnance;

public class OrdonnancesResponse {

    List<Ordonnance> ordonnances;
    ErrorCode error;

    public OrdonnancesResponse() {

    }

    public List<Ordonnance> getOrdonnances() {
        return ordonnances;
    }

    public void setOrdonnances(List<Ordonnance> ordonnances) {
        this.ordonnances = ordonnances;
    }

    public ErrorCode getError() {
        return error;
    }

    public void setError(ErrorCode error) {
        this.error = error;
    }

}
