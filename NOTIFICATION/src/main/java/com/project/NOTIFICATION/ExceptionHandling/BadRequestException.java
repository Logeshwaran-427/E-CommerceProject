package com.project.NOTIFICATION.ExceptionHandling;

public class BadRequestException extends RuntimeException {
    
    public BadRequestException(String message){
        super(message);
    }
}
