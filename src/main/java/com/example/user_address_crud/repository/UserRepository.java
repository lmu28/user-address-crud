package com.example.user_address_crud.repository;

import com.example.user_address_crud.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);


    boolean existsByAddressId(Long id);


    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.middleName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "u.phone LIKE CONCAT('%', :searchTerm, '%') OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<User> searchByAllFields(@Param("searchTerm") String searchTerm);




}