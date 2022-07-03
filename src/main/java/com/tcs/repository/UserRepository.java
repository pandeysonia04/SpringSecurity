package com.tcs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tcs.domain.Users;

public interface UserRepository extends JpaRepository<Users, Long> {

	Users findUserByUserName(String username);
	Users findUserByEmail(String email);
	
}
