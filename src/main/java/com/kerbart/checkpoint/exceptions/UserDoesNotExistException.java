package com.kerbart.checkpoint.exceptions;

public class UserDoesNotExistException extends Exception {

	public UserDoesNotExistException() {
		super();
	}

	public UserDoesNotExistException(String message) {
		super(message);
	}

	public UserDoesNotExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserDoesNotExistException(Throwable cause) {
		super(cause);
	}

}
