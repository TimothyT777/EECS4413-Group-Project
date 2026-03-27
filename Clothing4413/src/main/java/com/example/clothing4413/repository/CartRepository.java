package com.example.clothing4413.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.clothing4413.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByCustomerId(Long customerId);

    boolean existsByCustomerId(Long customerId);

    void deleteByCustomerId(Long customerId);

}