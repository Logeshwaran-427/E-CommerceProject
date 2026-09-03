package com.project.INVENTORY.ExceptionHandling;



public class BadRequestException extends RuntimeException {

    public BadRequestException(String msg){
        super(msg);
    }
    
}
