package com.project.NOTIFICATION.ExceptionHandling;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){
        super(message);
    }
}
