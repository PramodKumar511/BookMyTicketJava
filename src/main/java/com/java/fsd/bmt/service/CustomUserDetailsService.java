package com.java.fsd.bmt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.java.fsd.bmt.entity.UserEntity;
import com.java.fsd.bmt.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {

		UserEntity user = userRepository.findByEmailId(emailId)
				.orElseThrow(() -> new UsernameNotFoundException("User not exists by Email"));

//        Set<GrantedAuthority> authorities = user.getRoles().stream()
//                .map((role) -> new SimpleGrantedAuthority(role.getName()))
//                .collect(Collectors.toSet());

		return new org.springframework.security.core.userdetails.User(emailId, user.getPwd()
		// authorities
				, null);
	}
}