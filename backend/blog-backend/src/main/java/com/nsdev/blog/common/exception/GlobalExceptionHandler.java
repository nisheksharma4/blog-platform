package com.nsdev.blog.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.nsdev.blog.common.utils.ResponseStructure;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(BlogNotFoundException.class)
	public ResponseEntity<ResponseStructure<Void>> handleBlogNotFound(BlogNotFoundException b) {
		
		return ResponseStructure.create(
	            HttpStatus.NOT_FOUND,
	            b.getMessage(),
	            null
	    );
	}
	
	@ExceptionHandler(InvalidInputException.class)
	public ResponseEntity<ResponseStructure<Void>> handleInvalidInput(
	        InvalidInputException ex) {

	    return ResponseStructure.create(
	            HttpStatus.BAD_REQUEST,
	            ex.getMessage(),
	            null
	    );
	}

}
