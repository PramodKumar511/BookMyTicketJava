package com.java.fsd.bmt.model;

public class LoginStatus {
	
	private boolean status;
	
	
	public LoginStatus(){}

	public LoginStatus(boolean status) {
		super();
		this.status = status;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

}
