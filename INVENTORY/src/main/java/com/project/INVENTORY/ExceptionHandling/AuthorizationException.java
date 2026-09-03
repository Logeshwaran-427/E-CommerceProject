package com.project.INVENTORY.ExceptionHandling;

public class AuthorizationException extends RuntimeException {

    public AuthorizationException(String msg){
        super(msg);
    }

    
}
