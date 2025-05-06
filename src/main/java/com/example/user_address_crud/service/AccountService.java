package com.example.user_address_crud.service;

import com.example.user_address_crud.dto.AuthRequest;
import com.example.user_address_crud.dto.AuthResponse;

public interface AccountService {

    AuthResponse login(AuthRequest loginRequest);
    AuthResponse register(AuthRequest registerRequest);

}
