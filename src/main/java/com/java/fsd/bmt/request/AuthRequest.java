package com.java.fsd.bmt.request;

import lombok.Data;

@Data
public class AuthRequest {
	//@NotNull @Email @Length(min = 5, max = 50)
	private String emailId;
	
	//@NotNull @Length(min = 5, max = 10)
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	
	
	
}
