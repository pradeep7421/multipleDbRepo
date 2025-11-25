package com.multipledb.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Object> handleBookNotFound(BookNotFoundException ex) {
    	

    	    Map<String, Object> body = new HashMap<>();
    	    body.put("statusCode", ex.getStatusCode());
    	    body.put("status", ex.getStatus());
    	    body.put("message", ex.getMessage());

    	    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    

    }
    

}

