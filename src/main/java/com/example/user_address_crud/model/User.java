package com.example.user_address_crud.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String firstName;

    private String lastName;

    private String middleName;


    @Column(nullable = false)
    private String phone;


    @Column(nullable = false, unique = true)
    private String email;


    @Column(nullable = false)
    private LocalDate birthDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id", unique = true)
    private Address address;


}