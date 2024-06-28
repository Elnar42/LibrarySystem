package org.example.exceptions;

public class WrongEmailException extends RuntimeException{
    public WrongEmailException(String message){
        super(message);
    }
}
