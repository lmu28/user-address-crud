package com.example.user_address_crud.dto;

import com.example.user_address_crud.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(message = "Имя обязательно")
    private String firstName;

    private String lastName;
    private String middleName;

    @NotNull(message = "Телефон обязателен")
    @Pattern(regexp = "^\\d+$", message = "Телефон должен содержать только цифры")
    private String phone;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат email")
    private String email;

    @NotNull(message = "Дата рождения обязательна")
    @Past(message = "Дата рождения должна быть в прошлом")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private Long addressId;
    private AddressDto address;

    public static UserDto fromUser(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setMiddleName(user.getMiddleName());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setBirthDate(user.getBirthDate());

        if (user.getAddress() != null) {
            dto.setAddressId(user.getAddress().getId());
            dto.setAddress(AddressDto.fromAddress(user.getAddress()));
        }

        return dto;
    }

    public User toUser() {
        User user = new User();
        user.setId(this.id);
        user.setFirstName(this.firstName);
        user.setLastName(this.lastName);
        user.setMiddleName(this.middleName);
        user.setPhone(this.phone);
        user.setEmail(this.email);
        user.setBirthDate(this.birthDate);

        if (this.address != null) {
            user.setAddress(this.address.toAddress());
        }

        return user;
    }
}
