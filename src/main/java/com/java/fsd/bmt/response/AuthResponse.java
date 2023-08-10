package com.java.fsd.bmt.response;

public class AuthResponse {

	private String emailId;
	private String accessToken;
	private int uid;
	private String firstname;
	private String status;

	public AuthResponse() {
	}

	public AuthResponse(String emailId, String accessToken, int uid, String firstName, String status) {
		super();
		this.emailId = emailId;
		this.accessToken = accessToken;
		this.uid = uid;
		this.firstname = firstName;
		this.status = status;
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

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
