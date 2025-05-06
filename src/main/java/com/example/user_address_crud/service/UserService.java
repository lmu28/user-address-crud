package com.example.user_address_crud.service;



import com.example.user_address_crud.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {


    List<UserDto> findAll();


    Optional<UserDto> findById(Long id);


    UserDto save(UserDto userDto);


    Optional<UserDto> update(Long id, UserDto userDto);


    void deleteById(Long id);

//    List<UserDto> search(String searchTerm);
}