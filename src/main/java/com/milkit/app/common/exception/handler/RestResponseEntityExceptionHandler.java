package com.milkit.app.common.exception.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.milkit.app.common.ErrorCode;
import com.milkit.app.common.exception.ServiceException;
import com.milkit.app.common.response.GenericResponse;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { RuntimeException.class })
	protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
		GenericResponse response = new GenericResponse(ErrorCode.SystemError, ex.getMessage());
		
		log.error(ex.getMessage(), ex);
		
		return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
	
	@ExceptionHandler(value = { ServiceException.class })
	protected ResponseEntity<Object> handleServiceException(ServiceException ex, WebRequest request) {
		GenericResponse response = new GenericResponse(ex.getCode(), ex.getMessage());
		
		log.error(ex.getMessage(), ex);
		
		return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
	
}
