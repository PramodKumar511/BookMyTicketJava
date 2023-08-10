package com.java.fsd.bmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.java.fsd.bmt.entity.UserInfo;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    Optional<UserInfo> findByEmailId(String emailId);

}
