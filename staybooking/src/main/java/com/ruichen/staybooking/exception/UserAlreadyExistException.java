package com.ruichen.staybooking.exception;

//use runtime exception, we don't need try catch, there is a place to process all exceptions --> CostomExceptionHandler
public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
