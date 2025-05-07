package com.example.user_address_crud.controller;


import com.example.user_address_crud.dto.AuthRequest;
import com.example.user_address_crud.dto.AuthResponse;
import com.example.user_address_crud.dto.FieldError;
import com.example.user_address_crud.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);


    private final AccountService accountService;


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthRequest loginRequest) {
        log.info("Запрос на аутентификацию пользователя: {}", loginRequest.getUsername());
        AuthResponse response = accountService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid AuthRequest registerRequest) {
        log.info("Запрос на регистрацию пользователя: {}", registerRequest.getUsername());
        AuthResponse response = accountService.register(registerRequest);
        return ResponseEntity.ok(response);
    }


}