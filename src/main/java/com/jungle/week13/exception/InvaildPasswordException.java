package com.jungle.week13.exception;

public class InvaildPasswordException extends RuntimeException{
    public InvaildPasswordException(String message) {
        super(message);
    }
}
