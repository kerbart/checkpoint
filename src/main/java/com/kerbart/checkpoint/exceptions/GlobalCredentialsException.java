package com.kerbart.checkpoint.exceptions;

import com.kerbart.checkpoint.controller.responses.ErrorCode;

public class GlobalCredentialsException extends Exception {

	ErrorCode errorCode;
	
	public GlobalCredentialsException() {
		super();
	}

	public GlobalCredentialsException(String message) {
		super(message);
	}

	public GlobalCredentialsException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	
	public GlobalCredentialsException(String message, Throwable cause) {
		super(message, cause);
	}

	public GlobalCredentialsException(Throwable cause) {
		super(cause);
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	
	
}
