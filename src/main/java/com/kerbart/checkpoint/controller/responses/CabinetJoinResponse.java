package com.kerbart.checkpoint.controller.responses;

import com.kerbart.checkpoint.model.Cabinet;

public class CabinetJoinResponse {
	ErrorCode error;
	Cabinet cabinet;

	public CabinetJoinResponse() {

	}

	public Cabinet getCabinet() {
		return cabinet;
	}

	public void setCabinet(Cabinet cabinet) {
		this.cabinet = cabinet;
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
