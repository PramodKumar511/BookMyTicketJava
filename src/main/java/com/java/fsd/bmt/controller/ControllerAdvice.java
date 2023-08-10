package com.java.fsd.bmt.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.java.fsd.bmt.customexception.BookMyTicketException;
import com.java.fsd.bmt.customexception.EventException;
import com.java.fsd.bmt.model.HttpError;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*")
@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

	@ExceptionHandler
	public ResponseEntity<?> handleException(Throwable e, HttpServletRequest request) {
		HttpError httpError = new HttpError();
		if (e instanceof BookMyTicketException) {
			if (e.getMessage().contains("duplicate")) {
				setHttpErrorResponse(httpError, 403, "User Already Exist");
			} else {
				setHttpErrorResponse(httpError, 400, e.getMessage());
			}
		} else if (e instanceof BadCredentialsException) {
			setHttpErrorResponse(httpError, 401, "Invalid Credentials");
		} else if (e instanceof EventException) {

			setHttpErrorResponse(httpError, 500, e.getMessage());
		} else if (e instanceof UsernameNotFoundException) {

			setHttpErrorResponse(httpError, 401, e.getMessage());
		} else {
			setHttpErrorResponse(httpError, 500, "Internal server error !");
		}
		httpError.setTimestamp(LocalDateTime.now().toString());
		httpError.setPath(request.getRequestURI());
		return ResponseEntity.status(httpError.getStatus()).body(httpError);
	}

	private void setHttpErrorResponse(HttpError httpError, int status, String message) {
		httpError.setMessage(message);
		httpError.setStatus(status);
	}

}
