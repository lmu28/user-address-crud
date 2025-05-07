package com.example.user_address_crud.controller;

import com.example.user_address_crud.dto.AddressDto;
import com.example.user_address_crud.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private static final Logger log = LoggerFactory.getLogger(AddressController.class);

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<AddressDto>> getAllAddresses() {
        log.debug("Запрос на получение всех адресов");
        return ResponseEntity.ok(addressService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getAddressById(@PathVariable Long id) {
        log.debug("Запрос на получение адреса с ID: {}", id);
        return addressService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AddressDto> createAddress(@Valid @RequestBody AddressDto addressDto) {
        log.info("Запрос на создание адреса: {}", addressDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addressService.save(addressDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable Long id,
                                                    @Valid @RequestBody AddressDto addressDto) {
        log.info("Запрос на обновление адреса с ID: {}", id);
        return addressService.update(id, addressDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        log.info("Запрос на удаление адреса с ID: {}", id);
         addressService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<AddressDto>> searchAddresses(@RequestParam String query) {
        log.debug("Поиск адресов по запросу: {}", query);
        return ResponseEntity.ok(addressService.search(query));
    }
}