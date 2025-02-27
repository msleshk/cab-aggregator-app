package com.example.passengerservice.exception;

public class EmailAlreadyTakenException extends RuntimeException{
    public EmailAlreadyTakenException(String message){
        super(message);
    }
}
