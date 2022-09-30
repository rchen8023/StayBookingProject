package com.ruichen.staybooking.exception;

public class InvalidSearchDateException extends RuntimeException{
    public InvalidSearchDateException(String message) {
        super(message);
    }
}
