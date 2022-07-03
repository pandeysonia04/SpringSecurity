package com.tcs.filter;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcs.domain.HttpResponse;
import static com.tcs.constant.SecurityConstant.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class JWTAuthtenticationEntryPoint extends Http403ForbiddenEntryPoint {

	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
			throws IOException {
		
		HttpResponse httpResponse= new HttpResponse(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN,HttpStatus.FORBIDDEN.getReasonPhrase().toUpperCase(),FORBIDDEN_MESSAGE);
		
		response.setContentType(APPLICATION_JSON_VALUE);
		   response.setStatus(FORBIDDEN.value());
		   OutputStream outputStream= response.getOutputStream();
		   ObjectMapper mapper= new ObjectMapper();
		   mapper.writeValue(outputStream, httpResponse);
		 outputStream.flush();
		
	}

	
}
