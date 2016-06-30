package com.kerbart.checkpoint.controller.responses;

public enum ErrorCode {
    USER_BAD_CREDENTIALS, //
    USER_ALREADY_EXISTS, //
    USER_UNKNOWN, //
    USER_TOKEN_UNKNOWN,//
    USER_DOES_NOT_OWN_THIS_CABINET, //
    USER_ALREADY_OWN_THIS_CABINET, //
    USER_CREATION_ERROR, //
    CABINET_UNKNOWN, //
    PATIENT_UNKNOWN, //
    FILE_UPLOAD_ERROR
}
