package com.example.user_address_crud.service.impl;


import com.example.user_address_crud.dto.UserDto;
import com.example.user_address_crud.exception.FieldException;
import com.example.user_address_crud.model.Address;
import com.example.user_address_crud.model.User;
import com.example.user_address_crud.repository.AddressRepository;
import com.example.user_address_crud.repository.UserRepository;
import com.example.user_address_crud.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserDto::fromUser)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findById(Long id) {
        return userRepository.findById(id)
                .map(UserDto::fromUser);
    }

    @Override
    @Transactional
    public UserDto save(UserDto userDto) {
        if (userDto.getEmail() != null && userRepository.existsByEmail(userDto.getEmail())) {
            throw new FieldException("Пользователь с таким email уже существует","email");
        }

        User user = userDto.toUser();

        if (userDto.getAddressId() != null) {
            Address address = addressRepository.findById(userDto.getAddressId()).get();
            if (address.getUser() != null) throw new FieldException("Адрес занят другим пользователем", "addressId");
            user.setAddress(address);
        }

        user = userRepository.save(user);
        return UserDto.fromUser(user);
    }




    @Override
    @Transactional
    public Optional<UserDto> update(Long id, UserDto userDto) {

        if (userDto.getEmail() != null && userRepository.existsByEmailAndIdNot(userDto.getEmail(), id)) {
            throw new FieldException("Пользователь с таким email уже существует", "email");
        }

        Address address = null;
        if (userDto.getAddressId() != null) {
            address = addressRepository.findById(userDto.getAddressId())
                    .orElseThrow(() -> new EntityNotFoundException("Адрес с id " + userDto.getAddressId() + " не найден"));

            if (address.getUser() != null && !address.getUser().getId().equals(id)) {
                throw new FieldException("Адрес занят другим пользователем", "addressId");
            }
        }

        User updatedUser = userDto.toUser();
        updatedUser.setId(id);
        updatedUser.setAddress(address);

        return Optional.of(UserDto.fromUser(userRepository.save(updatedUser)));
    }


    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}