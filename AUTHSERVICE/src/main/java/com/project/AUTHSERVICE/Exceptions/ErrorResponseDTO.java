package com.project.AUTHSERVICE.Exceptions;

public class ErrorResponseDTO {

    String message;
    int status;

    public ErrorResponseDTO(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public ErrorResponseDTO() {
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
