package com.project.ORDER.ExceptionHandling;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String msg){
        super(msg);
    }
    
}
