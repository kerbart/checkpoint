package com.kerbart.checkpoint.exceptions;

public class UserAlreadyAssociatedException extends Exception {

	public UserAlreadyAssociatedException() {
		super();
	}

	public UserAlreadyAssociatedException(String message) {
		super(message);
	}

	public UserAlreadyAssociatedException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserAlreadyAssociatedException(Throwable cause) {
		super(cause);
	}

}
