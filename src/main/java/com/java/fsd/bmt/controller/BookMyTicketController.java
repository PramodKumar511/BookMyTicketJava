package com.java.fsd.bmt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.java.fsd.bmt.model.SignUp;
import com.java.fsd.bmt.model.UserDTO;
import com.java.fsd.bmt.response.AuthResponse;
import com.java.fsd.bmt.service.AuthService;
import com.java.fsd.bmt.service.BMTService;

@CrossOrigin("*")
@RestController("/bmt")
public class BookMyTicketController {

	@Autowired
	BMTService service;

	@Autowired
	AuthenticationManager authManager;

	@Autowired
	private AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<String> signUp(@RequestBody SignUp signup) {
		try {
			System.out.println("Inside signup method !!");

			if (!service.userSignUp(signup)) {
				return new ResponseEntity<>("Successfully signed up", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("User already exist", HttpStatus.OK);
			}

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

//	@GetMapping("/login")
//	public ResponseEntity<UserEntity> login(@RequestBody Login login) {
//		try {
//
//			UserEntity userDetails = service.verifyUserCred(login);
//
//			return new ResponseEntity<>(userDetails, HttpStatus.OK);
//
//		} catch (Exception e) {
//			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//
//	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> authenticate(@RequestBody UserDTO loginDto) {
		System.out.println("Inside Login method !!");
		String token = authService.login(loginDto);

		AuthResponse jwtAuthResponse = new AuthResponse();
		jwtAuthResponse.setAccessToken(token);

		return ResponseEntity.ok(jwtAuthResponse);
	}
}
