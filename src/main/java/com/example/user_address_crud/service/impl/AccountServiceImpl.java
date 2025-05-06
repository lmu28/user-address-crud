package com.example.user_address_crud.service.impl;


import com.example.user_address_crud.dto.AuthRequest;
import com.example.user_address_crud.dto.AuthResponse;
import com.example.user_address_crud.exception.FieldException;
import com.example.user_address_crud.model.Account;
import com.example.user_address_crud.repository.AccountRepository;
import com.example.user_address_crud.security.JwtTokenProvider;
import com.example.user_address_crud.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public AuthResponse login(AuthRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication.getName());

            return new AuthResponse(jwt);
        } catch (BadCredentialsException e) {
            throw new FieldException("Неверное имя пользователя или пароль", null);
        } catch (AuthenticationException e) {
            throw new FieldException("Ошибка аутентификации: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional
    public AuthResponse register(AuthRequest registerRequest) {
        if (accountRepository.existsByUsername(registerRequest.getUsername())) {
            throw new FieldException("Имя пользователя уже занято", "username");
        }

        Account account = new Account();
        account.setUsername(registerRequest.getUsername());
        account.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        accountRepository.save(account);

        return login(new AuthRequest(registerRequest.getUsername(), registerRequest.getPassword()));
    }



}