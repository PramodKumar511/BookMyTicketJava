package com.java.fsd.bmt.customexception;

public class BookMyTicketException extends Exception {

	private static final long serialVersionUID = 1L;

	public BookMyTicketException() {
		super();
	}

	public BookMyTicketException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BookMyTicketException(String message, Throwable cause) {
		super(message, cause);
	}

	public BookMyTicketException(String message) {
		super(message);
	}

	public BookMyTicketException(Throwable cause) {
		super(cause);
	}

}
