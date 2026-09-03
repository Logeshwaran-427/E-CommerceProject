package com.project.PAYMENT.Exceptions;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException(String msg){
        super(msg);
    }
}
