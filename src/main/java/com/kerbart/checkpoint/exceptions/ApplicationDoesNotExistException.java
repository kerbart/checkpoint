package com.kerbart.checkpoint.exceptions;

public class ApplicationDoesNotExistException extends Exception {

	public ApplicationDoesNotExistException() {
		super();
	}

	public ApplicationDoesNotExistException(String message) {
		super(message);
	}

	public ApplicationDoesNotExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationDoesNotExistException(Throwable cause) {
		super(cause);
	}

}
