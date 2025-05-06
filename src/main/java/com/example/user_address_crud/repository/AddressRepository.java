package com.example.user_address_crud.repository;


import com.example.user_address_crud.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {


    @Query("SELECT a FROM Address a WHERE " +
            "LOWER(a.region) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(a.city) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(a.street) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(a.houseNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(a.apartment) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Address> searchByAllFields(@Param("searchTerm") String searchTerm);
}