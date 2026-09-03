package com.project.ApiGateway.ExceptionHandling;

public class UnAuthorizedException extends RuntimeException {

    public UnAuthorizedException(String msg){
        super(msg);
    }
    
}
