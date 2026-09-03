package com.project.NOTIFICATION.ExceptionHandling;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandling {
    
    private final static Logger log=LoggerFactory.getLogger(GlobalExceptionHandling.class);

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorMessage> handleBadRequestException(BadRequestException ex){

        log.warn(ex.getMessage());
        ErrorMessage errorMessage=new ErrorMessage(400, ex.getMessage());

        return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorMessage> handleAuthorizationException(AuthorizationException ex){

        log.warn(ex.getMessage());
        ErrorMessage errorMessage=new ErrorMessage(403, ex.getMessage());

        return ResponseEntity.status(HttpStatus.SC_FORBIDDEN).body(errorMessage);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleBadResourceNotFoundException(ResourceNotFoundException ex){
        log.warn(ex.getMessage());
        ErrorMessage errorMessage=new ErrorMessage(404, ex.getMessage());

        return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(errorMessage);
    }

}
