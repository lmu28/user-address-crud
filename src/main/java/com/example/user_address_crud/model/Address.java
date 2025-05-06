package com.example.user_address_crud.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String region;


    @Column(nullable = false)
    private String city;


    @Column(nullable = false)
    private String street;


    @Column(nullable = false)
    private String houseNumber;

    private String apartment;

    @OneToOne(mappedBy = "address")
    private User user;

}