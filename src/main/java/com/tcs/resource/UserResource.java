package com.tcs.resource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import 	com.tcs.domain.UserPrincipal;
import com.tcs.domain.Users;
import com.tcs.exceptions.EmailExistsException;
import com.tcs.exceptions.EmailNotFoundException;
import com.tcs.exceptions.ExceptionHandling;
import com.tcs.exceptions.UserNameExistsException;
import com.tcs.exceptions.UserNotFoundException;
import com.tcs.service.UserService;
import com.tcs.utility.JWTTokenProvider;
import static com.tcs.constant.SecurityConstant.*;


@RestController
@RequestMapping(value="/user")
public class UserResource extends ExceptionHandling {
	
	

	private UserService userService;
	private AuthenticationManager authenticationManager;
	private JWTTokenProvider jwtTokenProvider;
	
	@Autowired
	public UserResource(UserService userService, AuthenticationManager authenticationManager,
			JWTTokenProvider jwtTokenProvider) {
		this.userService = userService;
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@PostMapping("/login")
	public ResponseEntity<Users> login(@RequestBody Users user) throws UserNotFoundException, UsernameNotFoundException, UserNameExistsException, EmailExistsException{
	authenticate(user.getUserName(), user.getPassword());
	Users loginUser = userService.findByUserName(user.getUserName());
    UserPrincipal userPrincipal = new UserPrincipal(loginUser);
    HttpHeaders jwtHeader=getJwtHeader(userPrincipal);
	return new ResponseEntity<>(loginUser, jwtHeader, HttpStatus.OK);
	}
	

	@PostMapping("/register")
	public ResponseEntity<Users> register(@RequestBody Users user) throws UserNotFoundException, UsernameNotFoundException, UserNameExistsException, EmailExistsException{
	Users newUser=	userService.register(user.getFirstName(), user.getLastName(), user.getUserName(), user.getEmail(), user.getPassword());
	return new ResponseEntity<>(newUser, HttpStatus.OK);
	}
	
	private HttpHeaders getJwtHeader(UserPrincipal userPrincipal) {
		HttpHeaders headers= new HttpHeaders();
		headers.add(JWT_TOKEN_HEADER , jwtTokenProvider.generateToken(userPrincipal));
	return headers;
	}

	private void authenticate(String userName, String password) {
	authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
		
	}
	
	

}
