package com.example.user_address_crud.service;



import com.example.user_address_crud.dto.AddressDto;

import java.util.List;
import java.util.Optional;

public interface AddressService {

    List<AddressDto> findAll();


    Optional<AddressDto> findById(Long id);


    AddressDto save(AddressDto addressDto);


    Optional<AddressDto> update(Long id, AddressDto addressDto);


    void deleteById(Long id);

    List<AddressDto> search(String searchTerm);
}