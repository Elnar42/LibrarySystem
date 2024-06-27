package org.example.exceptions;

public class WrongUsernameException extends RuntimeException{

    public WrongUsernameException(String message){
        super(message);
    }
}
