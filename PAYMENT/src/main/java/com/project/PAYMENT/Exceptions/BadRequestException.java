package com.project.PAYMENT.Exceptions;



public class BadRequestException extends RuntimeException {

    public BadRequestException(String msg){
        super(msg);
    }
    
}
