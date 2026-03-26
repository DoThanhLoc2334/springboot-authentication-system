package com.dtl.springboot_auth_system.exception;

public class TokenException extends RuntimeException {
    public TokenException(String message) {
        super(message);
    }
}