package com.java.fsd.bmt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.fsd.bmt.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {

	Optional<UserEntity> findByEmailId(String emailId);

	boolean existsByEmailId(String username);

}
