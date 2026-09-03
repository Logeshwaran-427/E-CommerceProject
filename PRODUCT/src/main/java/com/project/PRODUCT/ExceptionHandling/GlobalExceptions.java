package com.project.PRODUCT.ExceptionHandling;

import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice

public class GlobalExceptions {

    private static final Logger log=LoggerFactory.getLogger(GlobalExceptions.class);

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorMessage> handleAuthorizationException(AuthorizationException ex){

        log.warn(ex.getMessage());

        ErrorMessage errorMessage=new ErrorMessage();
        errorMessage.setMessage(ex.getMessage());
        errorMessage.setStatus(401);

        return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(errorMessage);

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleResourseException(ResourceNotFoundException ex){
        log.warn(ex.getMessage());

        ErrorMessage errorMessage=new ErrorMessage(ex.getMessage(),404);

        return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleUnExpectedException(Exception ex){
        log.error("Unexpected error occured", ex.getMessage());
        ErrorMessage errorMessage=new ErrorMessage(ex.getMessage(),500);
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(errorMessage);

    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorMessage> handleBadRequestException(BadRequestException ex){
        log.warn(ex.getMessage());
        ErrorMessage errorMessage=new ErrorMessage(ex.getMessage(),400);

        return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(errorMessage);

    }


    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorMessage> handleDuplicateException(BadRequestException ex){
        log.warn(ex.getMessage());
        ErrorMessage errorMessage=new ErrorMessage(ex.getMessage(),409);

        return ResponseEntity.status(HttpStatus.SC_CONFLICT).body(errorMessage);

    }
    
}
