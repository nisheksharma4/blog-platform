package com.nsdev.blog.common.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseStructure<T> {
	
	private int status;
	private String message;
	private T body;
	
	public static <T> ResponseEntity<ResponseStructure<T>> create(
            HttpStatus status,
            String message,
            T body) {

        ResponseStructure<T> response =
                new ResponseStructure<>(status.value(), message, body);

        return new ResponseEntity<>(response, status);
    }
}
