package com.example.driverservice.exception;

public class ResourceAlreadyTakenException extends RuntimeException {
    public ResourceAlreadyTakenException(String message) {
        super(message);
    }
}
