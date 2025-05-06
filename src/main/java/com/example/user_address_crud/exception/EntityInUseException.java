package com.example.user_address_crud.exception;

public class EntityInUseException extends RuntimeException {
  public EntityInUseException(String message) {
    super(message);
  }
}
