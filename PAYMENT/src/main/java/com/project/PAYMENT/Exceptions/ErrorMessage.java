package com.project.PAYMENT.Exceptions;

public class ErrorMessage {

    String message;
    int status;
    public ErrorMessage(String message, int status) {
        this.message = message;
        this.status = status;
    }
    public ErrorMessage() {
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    
    
}
