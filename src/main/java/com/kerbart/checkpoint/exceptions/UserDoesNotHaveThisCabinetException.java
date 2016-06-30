package com.kerbart.checkpoint.exceptions;

public class UserDoesNotHaveThisCabinetException extends Exception {

	public UserDoesNotHaveThisCabinetException() {
		super();
	}

	public UserDoesNotHaveThisCabinetException(String message) {
		super(message);
	}

	public UserDoesNotHaveThisCabinetException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserDoesNotHaveThisCabinetException(Throwable cause) {
		super(cause);
	}

}
