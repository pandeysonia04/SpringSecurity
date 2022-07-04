package com.tcs.exceptions;

import java.io.IOException;
import java.util.Objects;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.tcs.domain.HttpResponse;

@RestControllerAdvice
public class ExceptionHandling  {
	
	private final Logger logger=LoggerFactory.getLogger(getClass());
	
	private static final String ACCOUNT_LOCKED= "Your account has been locked. Please contact administration";
	private static final String METHOD_IS_NOT_AllOWED="This request method is not allowed on this endpoint. Please send a '%s' request";
	private static final String INTERNAL_SERVER_ERROR_MSG="An error occured while processing the request";
	private static final String INCORRECT_CREDENTIALS="UserName or Password is incorrect. Please try again ";
	private static final String ACCOUNT_DISABLED= "Your account has been disabled. If this is an error, please contact administration";
    private static final String  ERROR_PROCESSING_FILE ="Error occured while processing file";
    private static final String NOT_ENOUGH_PERMISSION=" you don't have enough permission";
    private static final String ERROR_PATH="/error";
    
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<HttpResponse> accountDisabledException(){
    	return createHttpResponse(HttpStatus.BAD_REQUEST, ACCOUNT_DISABLED);
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> badCredentialsException(){
    	return createHttpResponse(HttpStatus.BAD_REQUEST, INCORRECT_CREDENTIALS);
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpResponse> accessDeniedException(){
    	return createHttpResponse(HttpStatus.FORBIDDEN, NOT_ENOUGH_PERMISSION);
    }
    
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<HttpResponse> lockedException(){
    	return createHttpResponse(HttpStatus.UNAUTHORIZED, ACCOUNT_LOCKED);
    }
    
   @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<HttpResponse> tokeExpiredException(TokenExpiredException exception){
    	return createHttpResponse(HttpStatus.UNAUTHORIZED, exception.getMessage().toUpperCase());
    }
    
   @ExceptionHandler(EmailExistsException.class)
   public ResponseEntity<HttpResponse> emailExistsException(EmailExistsException exception){
   	return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage().toUpperCase());
   }
   
   @ExceptionHandler(UserNameExistsException.class)
   public ResponseEntity<HttpResponse> userNameExistsException(UserNameExistsException exception){
   	return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage().toUpperCase());
   }
   
   @ExceptionHandler(EmailNotFoundException.class)
   public ResponseEntity<HttpResponse> emailNotFoundException(EmailNotFoundException exception){
   	return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage().toUpperCase());
   }
   
   @ExceptionHandler(UserNotFoundException.class)
   public ResponseEntity<HttpResponse> userNotFoundException(UserNotFoundException exception){
   	return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage().toUpperCase());
   }
   
//   @ExceptionHandler(NoHandlerFoundException.class)
//   public ResponseEntity<HttpResponse> noHandlerFoundException(NoHandlerFoundException exception){
//   	return createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage().toUpperCase());
//   }
   
   @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
   public ResponseEntity<HttpResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException exception){
   	HttpMethod supportedMethod=Objects.requireNonNull(exception.getSupportedHttpMethods()).iterator().next();
	   
	   return createHttpResponse(HttpStatus.METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_AllOWED, supportedMethod));
   }
   
   @ExceptionHandler(Exception.class)
   public ResponseEntity<HttpResponse> internalServerErrorException(Exception exception){
   	return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
   }
  
   @ExceptionHandler(NoResultException.class)
   public ResponseEntity<HttpResponse> notFoundException(NoResultException exception){
   	return createHttpResponse(HttpStatus.NOT_FOUND, exception.getMessage());
   }
   
   @ExceptionHandler(IOException.class)
   public ResponseEntity<HttpResponse> iOException(Exception exception){
   	return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_PROCESSING_FILE);
   }
   
   
    
    ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message){
	
	HttpResponse httpResponse= new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),message.toUpperCase());
	
	return new ResponseEntity<HttpResponse>(httpResponse,httpStatus);
	
	

}
   
    

}
