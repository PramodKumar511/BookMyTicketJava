package com.java.fsd.bmt.service;

import com.java.fsd.bmt.model.UserDTO;

public interface AuthService {
	String login(UserDTO userDto);
}