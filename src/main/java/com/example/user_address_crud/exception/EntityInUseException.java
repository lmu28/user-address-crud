package com.example.user_address_crud.exception;

public class EntityInUseException extends IllegalStateException {
    public EntityInUseException(String message) {
        super(message);
    }
}
