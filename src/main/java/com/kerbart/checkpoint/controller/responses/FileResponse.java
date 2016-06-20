package com.kerbart.checkpoint.controller.responses;

public class FileResponse {

    String token;
    ErrorCode error;

    public FileResponse() {

    }

    public FileResponse(String token) {
        this();
        this.token = token;
    }

    public FileResponse(ErrorCode error) {
        this();
        this.error = error;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ErrorCode getError() {
        return error;
    }

    public void setError(ErrorCode error) {
        this.error = error;
    }

}
