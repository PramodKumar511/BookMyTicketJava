package com.java.fsd.bmt.response;

public class AuthResponse {

	private String emailId;
	private String accessToken;
	private String uid;

	public AuthResponse() {
	}

	public AuthResponse(String emailId, String accessToken, String uid) {
		super();
		this.emailId = emailId;
		this.accessToken = accessToken;
		this.uid = uid;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}
