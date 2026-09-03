package com.project.AUTHSERVICE.Exceptions;


public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException (String message){
        super(message);
    }

}
