package com.kerbart.checkpoint.controller.responses;

import com.kerbart.checkpoint.model.Cabinet;

public class ApplicationResponse {
	ErrorCode error;
	Cabinet application;
	
	public ApplicationResponse() {
		
	}

	public ApplicationResponse(ErrorCode error) {
		this();
		this.error = error;
	}

	public ErrorCode getError() {
		return error;
	}

	public void setError(ErrorCode error) {
		this.error = error;
	}

	public Cabinet getApplication() {
		return application;
	}

	public void setApplication(Cabinet application) {
		this.application = application;
	}

}
