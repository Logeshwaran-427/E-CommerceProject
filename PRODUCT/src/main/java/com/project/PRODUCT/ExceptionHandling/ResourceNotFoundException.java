package com.project.PRODUCT.ExceptionHandling;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String msg){
        super(msg);
    }
    
}
