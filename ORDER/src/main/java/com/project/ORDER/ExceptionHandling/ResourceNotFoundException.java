package com.project.ORDER.ExceptionHandling;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException (String msg){
        super(msg);
    }
    
}
