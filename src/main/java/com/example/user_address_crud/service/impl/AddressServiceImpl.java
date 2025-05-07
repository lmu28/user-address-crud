package com.example.user_address_crud.service.impl;


import com.example.user_address_crud.dto.AddressDto;
import com.example.user_address_crud.exception.EntityInUseException;
import com.example.user_address_crud.model.Address;
import com.example.user_address_crud.repository.AddressRepository;
import com.example.user_address_crud.repository.UserRepository;
import com.example.user_address_crud.service.AddressService;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private static final Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);


    private final AddressRepository addressRepository;
    private final UserRepository userRepository;


    @Override
    @Transactional(readOnly = true)
    public List<AddressDto> findAll() {
        return addressRepository.findAll()
                .stream()
                .map(AddressDto::fromAddress)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AddressDto> findById(Long id) {
        return addressRepository.findById(id)
                .map(AddressDto::fromAddress);
    }

    @Override
    @Transactional
    public AddressDto save(AddressDto addressDto) {
        log.info("Создание нового адреса");
        Address address = addressDto.toAddress();
        address = addressRepository.save(address);
        log.info("Создан адрес с ID: {}", address.getId());
        return AddressDto.fromAddress(address);
    }

    @Override
    @Transactional
    public Optional<AddressDto> update(Long id, AddressDto addressDto) {
        log.info("Обновление адреса с ID: {}", id);
        addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Адрес с ID " + id + " не найден"));

        Address address = addressDto.toAddress();
        address.setId(id);
        address = addressRepository.save(address);

        return Optional.of(AddressDto.fromAddress(address));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Удаление адреса с ID: {}", id);
        addressRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Адрес с ID " + id + " не найден"));


        if (userRepository.existsByAddressId(id)) {
            throw new EntityInUseException("Невозможно удалить адрес, так как он используется пользователем");
        }

        addressRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<AddressDto> search(String searchTerm) {
        return addressRepository.searchByAllFields(searchTerm).stream().map(AddressDto::fromAddress).toList();
    }
}