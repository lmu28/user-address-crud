package com.example.user_address_crud.exception;

public class FieldException extends RuntimeException {
    private String field;

    public FieldException(String message, String field) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
