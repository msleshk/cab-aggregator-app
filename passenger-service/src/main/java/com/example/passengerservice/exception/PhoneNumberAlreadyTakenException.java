package com.example.passengerservice.exception;

public class PhoneNumberAlreadyTakenException extends RuntimeException{
    public PhoneNumberAlreadyTakenException(String message){
        super(message);
    }

}
