package com.example.user_address_crud.controller;


import javax.validation.Valid;

import com.example.user_address_crud.dto.AuthRequest;
import com.example.user_address_crud.dto.AuthResponse;
import com.example.user_address_crud.dto.FieldError;
import com.example.user_address_crud.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AccountService accountService;


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthRequest loginRequest) {
        AuthResponse response = accountService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid AuthRequest registerRequest) {
        AuthResponse response = accountService.register(registerRequest);
        return ResponseEntity.ok(response);
    }


        //        if (result.hasErrors()) {
//            return handleValidationErrors(result);
//        }
//
//        // Проверка уникальности имени пользователя
//        if (!accountService.checkUsernameAvailability(registerRequest.getUsername())) {
//            Map<String, String> errors = new HashMap<>();
//            errors.put("username", "Имя пользователя уже занято");
//            return ResponseEntity.badRequest().body(Map.of("fieldErrors", errors));
//        }
//
//        try {
//            AuthResponse response = accountService.register(registerRequest);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest()
//                    .body(Map.of("message", e.getMessage()));
//        }


//    @GetMapping("/check-username")
//    public ResponseEntity<?> checkUsernameAvailability(@RequestParam String username) {
//        boolean available = accountService.checkUsernameAvailability(username);
//        return ResponseEntity.ok(Map.of("available", available));
//    }
//
    private ResponseEntity<?> handleValidationErrors(BindingResult result) {
        List<FieldError> errors = result.getFieldErrors().stream()
                .map(error -> new FieldError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errors);
    }
}