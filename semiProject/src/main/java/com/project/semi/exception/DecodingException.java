package com.project.semi.exception;

public class DecodingException extends RuntimeException {
    public DecodingException() {
    }
    public DecodingException(String message) {
        super(message);
    }
}