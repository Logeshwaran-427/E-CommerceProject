package com.project.NOTIFICATION.ExceptionHandling;

public class AuthorizationException extends RuntimeException {
    
    public AuthorizationException(String message){
        super(message);
    }

}
