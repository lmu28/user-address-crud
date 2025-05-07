package com.example.user_address_crud.exception;


import com.example.user_address_crud.dto.FieldError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(FieldException.class)
    public ResponseEntity<?> handleFieldException(FieldException  e) {
        log.warn("Ошибка валидации поля: {}, сообщение: {}", e.getField(), e.getMessage());
        List<FieldError> errors = new ArrayList<>();
            errors.add(new FieldError(e.getField(), e.getMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EntityInUseException.class)
    public ResponseEntity<?> handleEntityException(EntityInUseException  e) {
        log.warn("Ошибка сущности: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Map.of("message", e.getMessage())
        );
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.warn("Ошибки валидации: {}", ex.getBindingResult().getAllErrors());
        List<FieldError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnhandledExceptions(Exception e) {
        log.error("Необработанное исключение: ", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of("message", "Внутренняя ошибка сервера")
        );
    }


}
