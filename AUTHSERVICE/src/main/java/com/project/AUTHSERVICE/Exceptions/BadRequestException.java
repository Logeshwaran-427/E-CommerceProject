package com.project.AUTHSERVICE.Exceptions;

public class BadRequestException  extends RuntimeException{
    
    public BadRequestException(String msg){
        super(msg);
    }
}
