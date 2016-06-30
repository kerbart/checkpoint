package com.kerbart.checkpoint.controller.responses;

public class CabinetJoinResponse {
	ErrorCode error;
	
	public CabinetJoinResponse() {
		
	}

	public CabinetJoinResponse(ErrorCode error) {
		this();
		this.error = error;
	}

	public ErrorCode getError() {
		return error;
	}

	public void setError(ErrorCode error) {
		this.error = error;
	}
}
