package com.tcs.resource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.domain.Users;
import com.tcs.exceptions.EmailExistsException;
import com.tcs.exceptions.EmailNotFoundException;
import com.tcs.exceptions.ExceptionHandling;
import com.tcs.exceptions.UserNameExistsException;
import com.tcs.exceptions.UserNotFoundException;
import com.tcs.service.UserService;


@RestController
@RequestMapping(value="/user")
public class UserResource extends ExceptionHandling {
	
	private UserService userService;
	
	@Autowired
	public UserResource(UserService userService) {
		
		this.userService = userService;
	}

	
	
	@PostMapping("/register")
	public ResponseEntity<Users> register(@RequestBody Users user) throws UserNotFoundException, UsernameNotFoundException, UserNameExistsException, EmailExistsException{
	Users newUser=	userService.register(user.getFirstName(), user.getLastName(), user.getUserName(), user.getEmail(), user.getPassword());
	return new ResponseEntity<>(newUser, HttpStatus.OK);
	}
	

}
