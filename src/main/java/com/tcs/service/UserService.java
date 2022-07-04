package com.tcs.service;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.tcs.domain.Users;
import com.tcs.exceptions.EmailExistsException;
import com.tcs.exceptions.UserNameExistsException;
import com.tcs.exceptions.UserNotFoundException;

public interface UserService {

	Users register(String firstName, String lastName, String userName, String email, String password)throws UserNameExistsException, EmailExistsException, UsernameNotFoundException,UserNotFoundException;
 
	List<Users> getUsers();
 
	Users findByUserName(String userName);
	
	Users findByEmail(String email);

}
