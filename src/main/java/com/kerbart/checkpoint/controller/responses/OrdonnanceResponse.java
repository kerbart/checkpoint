package com.kerbart.checkpoint.controller.responses;

import com.kerbart.checkpoint.model.Ordonnance;

public class OrdonnanceResponse {

    Ordonnance ordonnance;
    ErrorCode error;

    public OrdonnanceResponse() {

    }

    public OrdonnanceResponse(Ordonnance ordonnance) {
        this();
        this.ordonnance = ordonnance;
    }

    public OrdonnanceResponse(ErrorCode error) {
        this();
        this.error = error;
    }

    public Ordonnance getOrdonnance() {
        return ordonnance;
    }

    public void setOrdonnance(Ordonnance ordonnance) {
        this.ordonnance = ordonnance;
    }

    public ErrorCode getError() {
        return error;
    }

    public void setError(ErrorCode error) {
        this.error = error;
    }

}
