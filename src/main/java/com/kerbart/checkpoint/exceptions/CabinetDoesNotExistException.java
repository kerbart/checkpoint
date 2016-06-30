package com.kerbart.checkpoint.exceptions;

public class CabinetDoesNotExistException extends Exception {

	public CabinetDoesNotExistException() {
		super();
	}

	public CabinetDoesNotExistException(String message) {
		super(message);
	}

	public CabinetDoesNotExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public CabinetDoesNotExistException(Throwable cause) {
		super(cause);
	}

}
