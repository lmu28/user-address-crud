package com.example.user_address_crud.exception;

public class AuthException extends RuntimeException {
    private String field;

    public AuthException(String message, String field) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
