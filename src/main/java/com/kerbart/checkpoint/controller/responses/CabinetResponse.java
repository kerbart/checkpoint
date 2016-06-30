package com.kerbart.checkpoint.controller.responses;

import com.kerbart.checkpoint.model.Cabinet;

public class CabinetResponse {
	ErrorCode error;
	Cabinet cabinet;
	
	public CabinetResponse() {
		
	}

	public CabinetResponse(ErrorCode error) {
		this();
		this.error = error;
	}

	public ErrorCode getError() {
		return error;
	}

	public void setError(ErrorCode error) {
		this.error = error;
	}

	public Cabinet getCabinet() {
		return cabinet;
	}

	public void setCabinet(Cabinet cabinet) {
		this.cabinet = cabinet;
	}

	

}
