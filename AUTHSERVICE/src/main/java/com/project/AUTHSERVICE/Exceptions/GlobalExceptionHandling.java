package com.project.AUTHSERVICE.Exceptions;

import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandling {

    public static final Logger log=LoggerFactory.getLogger(GlobalExceptionHandling.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResponseNotFound(ResourceNotFoundException exception){
        log.warn("Resource not found exception "+exception.getMessage());
        ErrorResponseDTO errorMessage=new ErrorResponseDTO(exception.getMessage(), 404);
        return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadRequestException(BadRequestException ex){
        log.warn(ex.getMessage());
        ErrorResponseDTO errorMessage=new ErrorResponseDTO(ex.getMessage(), 400);
        return ResponseEntity.status(HttpStatus.SC_BAD_GATEWAY).body(errorMessage);
    }
    
}
