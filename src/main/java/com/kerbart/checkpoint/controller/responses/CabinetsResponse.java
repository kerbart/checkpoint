package com.kerbart.checkpoint.controller.responses;

import java.util.List;

import com.kerbart.checkpoint.model.Cabinet;

public class CabinetsResponse {
	ErrorCode error;
	List<Cabinet> cabinets;
	
	public CabinetsResponse() {
		
	}

	public CabinetsResponse(ErrorCode error) {
		this();
		this.error = error;
	}

	public ErrorCode getError() {
		return error;
	}

	public void setError(ErrorCode error) {
		this.error = error;
	}

	public List<Cabinet> getCabinets() {
		return cabinets;
	}

	public void setCabinets(List<Cabinet> cabinets) {
		this.cabinets = cabinets;
	}
}
