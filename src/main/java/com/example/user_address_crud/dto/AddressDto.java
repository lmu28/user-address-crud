package com.example.user_address_crud.dto;

import com.example.user_address_crud.model.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    private Long id;

    @NotBlank(message = "Область обязательна")
    private String region;

    @NotBlank(message = "Город обязателен")
    private String city;

    @NotBlank(message = "Улица обязательна")
    private String street;

    @NotBlank(message = "Номер дома обязателен")
    private String houseNumber;

    private String apartment;


    public static AddressDto fromAddress(Address address) {
        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setRegion(address.getRegion());
        dto.setCity(address.getCity());
        dto.setStreet(address.getStreet());
        dto.setHouseNumber(address.getHouseNumber());
        dto.setApartment(address.getApartment());
        return dto;
    }

    public Address toAddress() {
        Address address = new Address();
        address.setId(this.id);
        address.setRegion(this.region);
        address.setCity(this.city);
        address.setStreet(this.street);
        address.setHouseNumber(this.houseNumber);
        address.setApartment(this.apartment);
        return address;
    }
}