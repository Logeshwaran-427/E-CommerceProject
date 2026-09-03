package com.project.ApiGateway.ExceptionHandling;

import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log=LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ErrorMessage> handleUnAuthorizedException(UnAuthorizedException ex){
        log.warn("Authorization header is missing");
        ErrorMessage errorMessage=new ErrorMessage(ex.getMessage(),401);
        return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body(errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleUnexpectedException(Exception ex){
        log.error("Unexpected error occurred", ex);
        ErrorMessage errorMessage=new ErrorMessage(ex.getMessage(),500);
        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(errorMessage);
    }
    
}
