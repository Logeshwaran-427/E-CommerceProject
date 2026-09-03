package com.project.PRODUCT.ExceptionHandling;



public class BadRequestException extends RuntimeException{

    public BadRequestException(String msg){
        super(msg);
    }
    
}
