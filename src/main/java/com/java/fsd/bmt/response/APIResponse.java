package com.java.fsd.bmt.response;

public class APIResponse {

	private Integer status;
	private Object data;
	private Object error;
	private String message;

	public APIResponse() {
	}

	public APIResponse(int statusCode, Object data, Object error, String message) {
		this.status = statusCode;
		this.data = data;
		this.error = error;
		this.message = message;
	}

	public APIResponse(int statusCode, String data, String error, String message) {

		this.status = statusCode;
		this.data = data;
		this.error = error;
		this.message = message;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getError() {
		return error;
	}

	public void setError(Object error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}