package org.example.exceptions;

public class WrongPhoneNumberException extends RuntimeException{

    public WrongPhoneNumberException(String message){
        super(message);
    }
}
