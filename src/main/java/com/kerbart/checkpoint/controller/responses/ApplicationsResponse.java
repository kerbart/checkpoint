package com.kerbart.checkpoint.controller.responses;

import java.util.List;

import com.kerbart.checkpoint.model.Cabinet;

public class ApplicationsResponse {
	ErrorCode error;
	List<Cabinet> applications;
	
	
	public ApplicationsResponse() {
		
	}

	public ApplicationsResponse(ErrorCode error) {
		this();
		this.error = error;
	}

	public ErrorCode getError() {
		return error;
	}

	public void setError(ErrorCode error) {
		this.error = error;
	}

	public List<Cabinet> getApplications() {
		return applications;
	}

	public void setApplications(List<Cabinet> applications) {
		this.applications = applications;
	}
	
	
}
