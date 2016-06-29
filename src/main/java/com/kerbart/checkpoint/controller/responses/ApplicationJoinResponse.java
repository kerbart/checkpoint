package com.kerbart.checkpoint.controller.responses;

public class ApplicationJoinResponse {
	ErrorCode error;
	
	public ApplicationJoinResponse() {
		
	}

	public ApplicationJoinResponse(ErrorCode error) {
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
