package com.tcs.service.implementation;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.tcs.domain.UserPrincipal;
import com.tcs.domain.Users;
import com.tcs.enumeration.Role;
import com.tcs.exceptions.EmailExistsException;
import com.tcs.exceptions.UserNameExistsException;
import com.tcs.repository.UserRepository;
import com.tcs.service.UserService;

import ch.qos.logback.classic.Logger;
import static com.tcs.constant.UserImplConstant.*;


@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService{

	
	
	private UserRepository userRepository;
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
	this.userRepository = userRepository;
	this.passwordEncoder=passwordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user= userRepository.findUserByUserName(username);
		if(user==null) {
			throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + username);
		}
		else {
			user.setLastLoginDateDisplay(user.getLastLoginDate());
			user.setLastLoginDate(new Date());
			userRepository.save(user);
			UserPrincipal userPrincipal = new UserPrincipal(user);
			return userPrincipal;
		}
		
	}
	
	@Override
	public Users register(String firstName, String lastName, String userName, String email,String password) throws UserNameExistsException, EmailExistsException, UsernameNotFoundException{
		validateNewUserNameAndEmail(StringUtils.EMPTY,userName , email);
		Users user= new Users();
		user.setUserId(generateUserId());
		String encodedPassword= encodePassword(password);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setUserName(userName);
		user.setEmail(email);
		user.setJoinDate(new Date());
		user.setPassword(encodedPassword);
		user.setActive(true);
		user.setNotLocked(true);
		user.setRoles(Role.ROLE_USER.name());
		user.setAuthorities(Role.ROLE_USER.getAuthorities());
		user.setProfileImageUrl(getTemporaryProfileImageUrl());
		userRepository.save(user);
		return user;
	}

	private String getTemporaryProfileImageUrl() {
		return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_USER_PATH).toUriString();
	}

	private String encodePassword(String password) {
		return passwordEncoder.encode(password);
	}

	

	private String generateUserId() {
		return RandomStringUtils.randomNumeric(10);
	}

	private Users validateNewUserNameAndEmail(String currentUserName, String newUserName, String email) throws UserNameExistsException, EmailExistsException {
		Users userByNewUserName= findByUserName(newUserName);
		Users userByNewEmail= findByUserName(email);
		if(StringUtils.isNotBlank(currentUserName)) {
			Users currentUser= findByUserName(currentUserName);
			if(currentUser==null) {
				throw new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME +currentUserName);
			}
			
			if(userByNewUserName!=null && !currentUser.getId().equals(userByNewUserName.getId())) {
				throw new UserNameExistsException(USER_NAME_ALREADY_EXISTS);
			}
			
			if(userByNewEmail!=null && !currentUser.getId().equals(userByNewEmail.getId())) {
				throw new EmailExistsException(EMAIL_ALREADY_EXISTS);
			}
			return currentUser;
			
			}
		else {
			
			if(userByNewUserName!=null) {
				throw new UserNameExistsException(USER_NAME_ALREADY_EXISTS);
			}
			
			if(userByNewEmail!=null ) {
				throw new EmailExistsException(EMAIL_ALREADY_EXISTS);
			}
			return null;
		}
		}
		


	@Override
	public List<Users> getUsers() {
		return userRepository.findAll();
	}

	@Override
	public Users findByUserName(String userName) {
		return userRepository.findUserByUserName(userName);
	}

	@Override
	public Users findByEmail(String email) {
		return userRepository.findUserByEmail(email);
	}


}
